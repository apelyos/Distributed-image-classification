package messages;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GenericMessage {
	@SuppressWarnings("unused")
	private GenericMessage() {
		//for jaxb
	}
	
	public GenericMessage(Object obj)  {
		try {
			from = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			from = "Unknown";
		}
		
		type = obj.getClass().getName();
		body = obj;
	}
	
    @XmlAttribute
    public String type;
 
    @XmlAttribute
    public String from;
 
    @XmlAnyElement(lax=true)
    public Object body;
    
    static final JAXBContext jc = initContext();

    private static JAXBContext initContext() {
        try {
			return JAXBContext.newInstance(GenericMessage.class, Job.class, Command.class, 
					Conclusion.class, JobResult.class, Summary.class);
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public static GenericMessage fromXML(String xml) throws JAXBException {
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringReader sr = new StringReader(xml);
        GenericMessage obj = (GenericMessage) unmarshaller.unmarshal(sr);
        return obj;
    }
    
    public String toXML() throws JAXBException {
		Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); //for nice indented output
        StringWriter sw = new StringWriter();
        marshaller.marshal(this, sw);
        return sw.toString();
    }
}
