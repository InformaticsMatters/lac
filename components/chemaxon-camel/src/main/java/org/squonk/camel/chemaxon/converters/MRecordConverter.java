package org.squonk.camel.chemaxon.converters;

import chemaxon.marvin.io.MRecord;
import org.squonk.chemaxon.molecule.MoleculeUtils;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;

import java.util.Map;

/**
 * Created by timbo on 21/04/2014.
 */
@Converter
public class MRecordConverter {

    @Converter
    public static Map<String, String> convertToMap(MRecord record, Exchange exchange) {
        return MoleculeUtils.mrecordToMap(record);
    }
    
    @Converter
    public static String convertToString(MRecord record, Exchange exchange) {
        return record.getString();
    }
    
    
}