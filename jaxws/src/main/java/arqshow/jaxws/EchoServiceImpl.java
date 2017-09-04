package arqshow.jaxws;

import javax.jws.WebService;

@WebService(endpointInterface = "arqshow.jaxws.EchoService", serviceName = "EchoService")
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String msg) {
        return msg;
    }
}
