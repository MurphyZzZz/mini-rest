package minirest;

import minidi.container.Container;
import minidi.container.ContainerSupplier;
import minidi.container.MiniDi;
import minirest.server.Server;
import org.reflections.Reflections;

public class MiniRest {
    Class<?> applicationClz;

    public MiniRest(Class<?> applicationClz) {
        this.applicationClz = applicationClz;
    }

    public void start() throws Exception {
        ContainerSupplier containerSupplier = new ContainerSupplier(applicationClz);
        this.registerBeansForMiniRest(containerSupplier.getContainer());
        containerSupplier.bootStrap();
        Server server = (Server) containerSupplier.getContainer().getBeanInstance(Server.class);
        server.run();
    }

    private void registerBeansForMiniRest(Container container) {
        Reflections reflections = new Reflections(this.getClass().getPackageName());
        reflections.getTypesAnnotatedWith(MiniDi.class).forEach(container::registerBean);
    }
}
