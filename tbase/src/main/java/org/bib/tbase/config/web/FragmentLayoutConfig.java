package org.bib.tbase.config.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.display.YesNoAnyDisplayable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FragmentLayoutConfig {
// Constructors

// Public Methods
	@Bean
	public FragmentLayout fragmentLayout() {
		FragmentLayout fragmentLayout = new FragmentLayout();
		
		// Horizontal Form - Text Field Fragment - Text input is small width
		fragmentLayout.set("horzForm-sm-labelWidthClass", "col-md-2");
		fragmentLayout.set("horzForm-sm-inputWidthClass", "col-md-1");
		fragmentLayout.set("horzForm-sm-errorWidthClass", "col-md-8");
				
		// Horizontal Form - Text Field Fragment - Text input is medium width
		fragmentLayout.set("horzForm-md-labelWidthClass", "col-md-2");
		fragmentLayout.set("horzForm-md-inputWidthClass", "col-md-3");
		fragmentLayout.set("horzForm-md-errorWidthClass", "col-md-7");
		
		// Horizontal Form - Text Field Fragment - Text input is large width
		fragmentLayout.set("horzForm-lg-labelWidthClass", "col-md-2");
		fragmentLayout.set("horzForm-lg-inputWidthClass", "col-md-5");
		fragmentLayout.set("horzForm-lg-errorWidthClass", "col-md-5");

		// Horizontal Form - Inline Select
		fragmentLayout.set("horzForm-inlineSelect-labelWidthClass", "col-md-2");
		fragmentLayout.set("horzForm-inlineSelect-inputWidthClass", "col-md-10");
		fragmentLayout.set("horzForm-inlineSelect-errorWidthClass", "col-md-10 col-md-offset-2");
		
		// Horizontal Form - Drop Down (Resuses text field fragment sizing for label and input)
		fragmentLayout.set("horzForm-dropDown-errorWidthClass", "col-md-10 col-md-offset-2");
		
		
		/*
		 * Vertical Form
		 */
		// Vertical Form - Text Field Fragment - form-group container widths
		fragmentLayout.set("vertForm-sm-containerWidthClass", "col-md-2");
		fragmentLayout.set("vertForm-md-containerWidthClass", "col-md-6");
		fragmentLayout.set("vertForm-lg-containerWidthClass", "col-md-12");
		
		/*
		 * Inline Form
		 */
		fragmentLayout.set("inlineForm-xs-containerWidthClass", "col-sm-2");
		fragmentLayout.set("inlineForm-sm-containerWidthClass", "col-sm-4");
		fragmentLayout.set("inlineForm-md-containerWidthClass", "col-sm-6");
		fragmentLayout.set("inlineForm-lg-containerWidthClass", "col-sm-8");
		fragmentLayout.set("inlineForm-xl-containerWidthClass", "col-sm-10");
		fragmentLayout.set("inlineForm-xxl-containerWidthClass", "col-sm-12");
		
		return fragmentLayout;
	}
	
	/**
	 * Values for the enum YesNoAnyDisplayable.  Used to present Yes/No/Either option set on the UI.
	 * @return
	 */
	@Bean("yesNoAnyValues")
	public YesNoAnyDisplayable[] yesNoAnyDisplayableValues() {
		return YesNoAnyDisplayable.values();
	}

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(FragmentLayoutConfig.class);
	
// Helper Class
	public static class FragmentLayout implements Serializable {
		// Constructors
		public FragmentLayout() {
			
		}
		
		// Public Methods
		public String get(String key) {
			return StringUtils.trimToEmpty(layoutMap.get(key));
		}
		
		public void set(String key, String value) {
			layoutMap.put(key, value);
		}
		
		// Getters & Setters
		public Map<String, String> getLayoutMap() {
			return layoutMap;
		}
		
		public void setLayoutMap(Map<String, String> layoutMap) {
			if(layoutMap == null) {
				this.layoutMap = new HashMap<String, String>();
			}
			else {
				this.layoutMap = layoutMap;
			}
		}
		
		// Attributes
		/**
		 * Serialization
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Map containing fragment layout style class mappings
		 */
		private Map<String, String> layoutMap = new HashMap<String, String>();
	}
}
