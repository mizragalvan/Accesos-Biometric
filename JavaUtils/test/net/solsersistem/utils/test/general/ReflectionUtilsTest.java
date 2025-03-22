package mx.solsersistem.utils.test.general;

import mx.solsersistem.utils.general.ReflectionUtils;
import mx.solsersistem.utils.general.exceptions.InvalidFieldException;

import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilsTest {
    
    public class TestClass {
        private String field1;
        private Integer field2;
        
        public final String getField1() {
            return this.field1;
        }
        
        public final void setField1(final String field1Parameter) {
            this.field1 = field1Parameter;
        }
        
        public final Integer getField2() {
            return this.field2;
        }
        
        public final void setField2(final Integer field2Parameter) {
            this.field2 = field2Parameter;
        }
    }
    
    public class PrivateClass {
        private String privateField;
        
        public final void setPrivateField1(final String field1Parameter) {
            this.privateField = field1Parameter;
        }
        
        public final String publicGetField1() {
            return this.getPrivateField1();
        }
        
        private String getPrivateField1() {
            return this.privateField;
        }
    }

    @Test
    public final void whenGetValueThenReturnObjectValue() throws InvalidFieldException {
        final String testString = "Test1";
        final TestClass testObject = this.createTestClassObject(testString, 1);
        Assert.assertEquals("Error al recuperar el valor del campo",
                testString, ReflectionUtils.getValue(testObject, "field1"));
    }
    
    @Test(expected = InvalidFieldException.class)
    public final void whenGetValueErrorThenInvalidFieldException() throws InvalidFieldException {
        final TestClass testClass = this.createTestClassObject("Test2", 1);
        ReflectionUtils.getValue(testClass, "field3");
    }
    
    @Test(expected = InvalidFieldException.class)
    public final void whenGetValuePrivateAccessErrorThenInvalidFieldException() throws InvalidFieldException {
        final PrivateClass testClass = this.createPrivateClassObject("Test3");
        ReflectionUtils.getValue(testClass, "field");
    }

    private PrivateClass createPrivateClassObject(final String string) {
        final PrivateClass privateClass = new PrivateClass();
        return privateClass;
    }

    private TestClass createTestClassObject(final String stringParameter, final int intParameter) {
        final TestClass testClass = new TestClass();
        testClass.setField1(stringParameter);
        testClass.setField2(new Integer(1));
        return testClass;
    }
}
