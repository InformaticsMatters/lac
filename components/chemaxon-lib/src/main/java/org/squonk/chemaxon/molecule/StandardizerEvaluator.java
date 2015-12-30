package org.squonk.chemaxon.molecule;

import chemaxon.formats.MolFormatException;
import chemaxon.standardizer.Standardizer;
import chemaxon.struc.Molecule;
import com.im.lac.types.MoleculeObject;
import com.im.lac.util.Pool;
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
    public MoleculeEvaluator.Mode getMode() {
        return MoleculeEvaluator.Mode.Transform;
    }
    
    @Override
    public Molecule processMolecule(Molecule mol) {
        Standardizer szr = pool.checkout();
        try {
            szr.standardize(mol);
        } finally {
            pool.checkin(szr);
        }
        return mol;
    }
   
    @Override
    public MoleculeObject processMoleculeObject(MoleculeObject mo) throws MolFormatException, IOException {
        Molecule mol = MoleculeUtils.fetchMolecule(mo, false);
        mol = processMolecule(mol);
        return MoleculeUtils.derriveMoleculeObject(mo, mol, mo.getFormat("mol"));
    }
    
     @Override
    public Map<String, Object> getResults(Molecule mol) {
        // could handle the actions that were performed? 
        return Collections.emptyMap();
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