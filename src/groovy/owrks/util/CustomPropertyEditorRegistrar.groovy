package owrks.util

import java.text.SimpleDateFormat
import java.util.Date

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.springframework.beans.propertyeditors.CustomDateEditor

public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
	
  public void registerCustomEditors(PropertyEditorRegistry registry) {
	  registry.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd.MM.yyyy"), true));
  }
  
}
