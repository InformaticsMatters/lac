import chemaxon.util.ConnectionHandler
import org.squonk.camel.chemaxon.processor.db.DefaultJChemInserter
import org.squonk.camel.processor.ChunkBasedReporter
import org.squonk.chemaxon.molecule.MoleculeUtils
import groovy.sql.Sql
import org.apache.camel.CamelContext
import org.apache.camel.ProducerTemplate
import org.apache.camel.builder.RouteBuilder

/**
 *
 * @author timbo
 */
class EMoleculesLoader extends AbstractLoader {
   
    static void main(String[] args) {
        println "Running with args: $args"
        def instance = new EMoleculesLoader('emolecules.properties')
        if (args.length > 0) {
            instance.run(args)
        }
    }
    
    EMoleculesLoader(String config) {
        super(new File(config).toURL())
    }
    
    void executeRoutes(CamelContext camelContext) {
        ProducerTemplate t = camelContext.createProducerTemplate()
        String file = props.path + '/' + props.file
        println "Loading file $file"
        t.sendBody('direct:start', new File(file))
        println "Finished loading"
                              
        Sql db = new Sql(dataSource.getConnection())
        int rows = db.firstRow('select count(*) from ' + database.vendordbs.schema + '.' + props.table)[0]
        println "Table ${props.table} now has $rows rows"
    }
    
    void createRoutes(CamelContext camelContext) {
        final String cols = getColumnNamesFromColumnDefs(props.extraColumnDefs).join(',')
        //println "extracols = $cols"

        DefaultJChemInserter updateHandlerProcessor = new DefaultJChemInserter(
            database.vendordbs.schema + '.' + props.table, cols, props.fields)

        ConnectionHandler conh = new ConnectionHandler(dataSource.getConnection(), database.vendordbs.schema + '.jchemproperties')
        updateHandlerProcessor.connectionHandler = conh
        
        camelContext.addRoutes(new RouteBuilder() {
                def void configure() {
                    onException()
                    .handled(true)
                    .to('direct:errors')
            
                    from('direct:start')
                    .split().method(MoleculeUtils.class, 'mrecordIterator').streaming()
                    //.log('Processing line ${header.CamelSplitIndex}')
                    .process(updateHandlerProcessor)
                    .process(new ChunkBasedReporter(props.reportingChunk))
            
                    from('direct:errors')
                    .log('Error: ${exception.message}')
                    //.log('Error: ${exception.stacktrace}')
                    .transform(body().append('\n'))
                    .to('file:' + props.path + '?fileExist=Append&fileName=' + props.file + '_errors')
                }
            })
    }
	
}

