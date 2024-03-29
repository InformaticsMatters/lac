package org.squonk.chemaxon.molecule;

import chemaxon.formats.MolFormatException;
import chemaxon.standardizer.Standardizer;
import chemaxon.struc.Molecule;
import org.squonk.types.MoleculeObject;
import org.squonk.util.ExecutionStats;
import org.squonk.util.Pool;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author timbo
 */
public class StandardizerEvaluator implements MoleculeEvaluator {
    
    private final StandardizerPool pool;
    
    public StandardizerEvaluator(String szr, int poolSize) {
        this.pool = new StandardizerPool(szr, poolSize);
    }


    @Override
    public String getDescription() {
        return "Standardizer: " + pool.szr;
    }

    @Override
    public String getPropName() {
        return null;
    }

    @Override
    public MoleculeEvaluator.Mode getMode() {
        return MoleculeEvaluator.Mode.Transform;
    }
    
    @Override
    public Molecule processMolecule(Molecule mol, Map<String,Integer> stats) {
        if (mol == null) {
            return null;
        }
        Standardizer szr = pool.checkout();
        try {
            szr.standardize(mol);
            ExecutionStats.increment(stats, getMetricsCode(),1);
        } finally {
            pool.checkin(szr);
        }
        return mol;
    }
   
    @Override
    public MoleculeObject processMoleculeObject(MoleculeObject mo, Map<String,Integer> stats) throws MolFormatException, IOException {
        if (mo == null || mo.getSource() == null) {
            return mo;
        }

        Molecule mol = MoleculeUtils.fetchMolecule(mo, false);
        mol = processMolecule(mol, stats);
        return MoleculeUtils.deriveMoleculeObject(mo, mol, mo.getFormat("mol"));
    }
    
     @Override
    public Map<String, Object> getResults(Molecule mol) {
        // could handle the actions that were performed? 
        return Collections.emptyMap();
    }

    @Override
    public String getKey() {
        return "CXN_Standardizer";
    }

    @Override
    public String getMetricsCode() {
        return "CXN.Standardize";
    }

    class StandardizerPool extends Pool<Standardizer> {
        
        final String szr;
        
        StandardizerPool(String szr, int size) {
            super(size);
            this.szr = szr;
        }
        
        @Override
        protected Standardizer create() {
            return new Standardizer(szr);
        }
    }
    
}
