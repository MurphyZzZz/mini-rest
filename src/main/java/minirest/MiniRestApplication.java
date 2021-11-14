package minirest;

import container.ContainerSupplier;
import minirest.server.Server;

public class MiniRestApplication {

    public static void main(String[] args) throws Exception {
        ContainerSupplier containerSupplier = new ContainerSupplier(MiniRestApplication.class);
        containerSupplier.bootStrap();
        Server server = (Server) containerSupplier.getContainer().getBeanInstance(Server.class);
        server.run();
    }
}
