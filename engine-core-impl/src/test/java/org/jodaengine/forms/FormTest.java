package org.jodaengine.forms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jodaengine.util.io.FileStreamSource;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests the conversion of the formFile input to the internal representation.
 */

public class FormTest {
    private static final String FORM_PATH = "src/test/resources/org/jodaengine/forms/";
    private static final String FORM_LOCATION = FORM_PATH + "testForm.html";
    
  /**
   * Tests the setting of the formfield variables.
   */
  @Test
  public void testFormCreation() {
      FileStreamSource source = new FileStreamSource(new File(FORM_LOCATION));
      Form form = new FormImpl("form", source);
      JodaFormField field1 = form.getFormField("claimPoint1");
      JodaFormField field2 = form.getFormField("claimPoint2");
      
      Assert.assertEquals(field1.getOutputExpression(), "#{claimPoint1}");
      Assert.assertEquals(field1.getInputVariable(), "claimPoint1");
      Assert.assertEquals(field1.getInputExpression(), "#{claimPoint1}");
      Assert.assertEquals(field1.getOutputVariable(), "claimPoint1");
      Assert.assertEquals(field2.getOutputExpression(), "#{claimPoint2}");
      Assert.assertEquals(field2.getInputVariable(), "claimPoint2");
      Assert.assertEquals(field2.getInputExpression(), "#{claimPoint2}");
      Assert.assertEquals(field2.getOutputVariable(), "claimPoint2");
  }
  
  /**
   * Tests the setting of the class assignment (classes, the input should be converted to) to form fields.
   */
  @Test
  public void testFormClassAssignment() {
      FileStreamSource source = new FileStreamSource(new File(FORM_LOCATION));
      Form form = new FormImpl("form", source);
      JodaFormField field1 = form.getFormField("claimPoint1");
      JodaFormField field2 = form.getFormField("claimPoint2");
      
      Assert.assertEquals(field1.getDataClazz(), String.class);
      Assert.assertEquals(field2.getDataClazz(), Integer.class);
  }
  
  /**
   * Reads a file and returns its content as a String.
   * 
   * @param fileName
   *            the file name
   * @return the string
   */
  private static String readFile(String fileName) {

      String fileContent = "";
      File file = new File(fileName);
      FileReader input;
      try {
          input = new FileReader(file);
          BufferedReader reader = new BufferedReader(input);

          String nextLine = reader.readLine();
          while (nextLine != null) {
              fileContent = fileContent.concat(nextLine);
              nextLine = reader.readLine();
          }

          reader.close();
          input.close();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }

      fileContent = fileContent.trim();
      return fileContent;
  }
}
