package org.bib.tbase.dbunit;

import java.io.InputStream;
import java.util.Date;

import org.apache.poi.util.IOUtils;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.joda.time.DateTime;
import org.springframework.core.io.Resource;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;

/**
 * Data Set Loader that enables column sensing mode and replaces [null] with null, [now] with current date/time, [yearago] with current date/time - year.
 */
public class ColumnSensingReplacementDataSetLoader extends AbstractDataSetLoader {
// Constructors

// Public Methods
	@Override
	protected IDataSet createDataSet(Resource resource) throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        
        InputStream inputStream = null;
        ReplacementDataSet replacementDataSet = null;
        
        try {
        	inputStream = resource.getInputStream();
        	
        	replacementDataSet = createReplacementDataSet(builder.build(inputStream));
        }
        finally {
        	IOUtils.closeQuietly(inputStream);
        }
        
        return replacementDataSet;
	}

// Priavte Methods
	private ReplacementDataSet createReplacementDataSet(FlatXmlDataSet dataSet) {
        ReplacementDataSet replacementDataSet = new ReplacementDataSet(dataSet);
         
        // Configure the replacement dataset to replace '[null]' strings with null.
        replacementDataSet.addReplacementObject("[null]", null);
        replacementDataSet.addReplacementObject("[now]", new Date());
        replacementDataSet.addReplacementObject("[yearago]", new DateTime().minusYears(1).toDate());
        replacementDataSet.addReplacementObject("[yearahead]", new DateTime().plusYears(1).toDate());
         
        return replacementDataSet;
    }
	
// Getters & Setters

// Attributes
}
