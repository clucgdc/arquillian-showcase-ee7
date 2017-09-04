package arqshow.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface EchoService {
    
    @WebMethod
    String echo(String msg);
}
