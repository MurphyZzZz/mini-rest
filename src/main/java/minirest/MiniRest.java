package minirest;

import container.ContainerSupplier;
import minirest.server.Server;

public class MiniRest {
    Class<?> applicationClz;

    public MiniRest(Class<?> applicationClz) {
        this.applicationClz = applicationClz;
    }

    public void start() throws Exception {
        ContainerSupplier containerSupplier = new ContainerSupplier(applicationClz);
        containerSupplier.bootStrap();
        Server server = (Server) containerSupplier.getContainer().getBeanInstance(Server.class);
        server.run();
    }
}
