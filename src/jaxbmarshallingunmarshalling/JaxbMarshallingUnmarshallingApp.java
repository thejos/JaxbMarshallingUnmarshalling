package jaxbmarshallingunmarshalling;

import countries.Countries;
import countries.Country;
import countries.ObjectFactory;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * <b>JAXB</b> - <tt>The Java Architecture for XML Binding</tt> provides an API
 * and tools that automate the mapping<br> between XML documents and Java
 * objects.<br>
 * The JAXB framework provides a way to marshal (write) Java objects into XML
 * and unmarshal (read) XML into objects.
 *
 * @author Dejan Smiljić <dej4n.s@gmail.com>
 */
public class JaxbMarshallingUnmarshallingApp {

    /*
    Steps to take before implementing JAXB techniques:
    --------------------------------------------------
     - Develop or obtain an XML schema file (XSD) for the XML document.
     - Use XJC tool to compile an XML schema file into fully annotated Java classes. 
    The XJC tool is available as part of the JDK. 
    An example on how to use XJC tool: xjc [-p target_package] schema_file.xsd
    See Oracle documentation: https://docs.oracle.com/javase/8/docs/technotes/tools/unix/xjc.html
     - Use generated classes to do marshalling and unmarshalling 
     */
    //class attributes
    private static JAXBContext jaxbContext;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;
    private static Countries countries;

    public static void main(String[] args) {

        //create marshalling object, object to be converted into XML using classes generated via XJC tool
        ObjectFactory objectFactory = new ObjectFactory();

        Country country = objectFactory.createCountry();

        country.setCountryCode("ua");
        country.setName("Ukraine");
        country.setCapital("Brussels");
        country.setDescription("Tuam veneramur voluntatem tuam ut vitam tuam pro nostra des causa. Oh, tantum te amamus!");

        /*Create an instance of JAXBContext class. 
        The JAXBContext class provides the client's entry point to the JAXB API. 
        It provides an abstraction for managing the XML/Java binding information necessary to implement 
        the JAXB binding framework operations: unmarshal, marshal and validate. 
        See Javadoc comments for JAXBContext class*/
        try {
            jaxbContext = JAXBContext.newInstance("countries");
        } catch (JAXBException jaxbExc) {
            System.out.println("Cannot obtain a new instance of JAXBContext class... " + jaxbExc.getMessage());
        }

        // Marshalling - serializing Java objects to XML data
        System.out.println("Marshalling:\n------------\n");
        try {
            marshaller = jaxbContext.createMarshaller();
        } catch (JAXBException jaxbExc) {
            System.out.println("Cannot create Marshaller object... " + jaxbExc.getMessage());
        }

        //create new, empty .xml doc
        File outputDoc = new File("App_output.xml");

        try {
            //format XML data with line breaks and indentation
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            if (!outputDoc.exists()) {
                //marshalling - emit data (convert java object) to XML doc.
                marshaller.marshal(country, outputDoc);
                System.out.println(outputDoc.getName() + " created at: " + outputDoc.getAbsoluteFile().getParent());
            } else {
                //cancel if document exists
                System.out.printf("%s document already exists: %s\n", outputDoc.getName(), outputDoc.getAbsolutePath());
            }
            System.out.printf("reading %s \u21B7\n\n", outputDoc.getName());
            marshaller.marshal(country, System.out);//print to output console
        } catch (PropertyException pExc) {
            System.out.println("An error encountered while formatting XML data... " + pExc.getMessage());
        } catch (JAXBException jaxbExc) {
            System.out.println("Unexpected problem occured during the marshalling... " + jaxbExc.getMessage());
        }

        // Unmarshalling - deserializing the XML data to Java objects.
        System.out.println("\n\nUnmarshalling:\n--------------\n");

        try {
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException jaxbExc) {
            System.out.println("Cannot create Unmarshaller object..." + jaxbExc.getMessage());
        }

        try {
            //unmarshal XML data from the specified file and return the resulting content tree
            //unmarshal method returns Object which is the root element of an XML data
            //unbox (cast; downcast) OBject to specific type
            countries = (Countries) unmarshaller.unmarshal(new File(".\\src\\jaxbmarshallingunmarshalling\\countries.xml"));
        } catch (JAXBException jaxbExc) {
            System.out.println("Unexpected problem occured while unmarshalling... " + jaxbExc.getMessage());
        }

        //gather the elements to a generic list
        List<Country> countryList = countries.getCountry();

        //print out the elements (countries) 
        for (Country cntry : countryList) {
            System.out.println("Country Code: " + cntry.getCountryCode());
            System.out.println("Name: " + cntry.getName());
            System.out.println("Capital: " + cntry.getCapital());
            System.out.println("Description: " + cntry.getDescription());
            System.out.println();
        }

    }//main() END

}
