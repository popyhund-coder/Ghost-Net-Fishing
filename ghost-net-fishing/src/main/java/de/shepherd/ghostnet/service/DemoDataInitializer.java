package de.shepherd.ghostnet.service;

import de.shepherd.ghostnet.entity.GhostNet;
import de.shepherd.ghostnet.entity.GhostNetStatus;
import de.shepherd.ghostnet.entity.Rescuer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Singleton
@Startup
@DataSourceDefinition(
        name = "java:app/jdbc/GhostNetDS",
        className = "com.mysql.cj.jdbc.MysqlDataSource",
        url = "jdbc:mysql://localhost:3306/ghost_net?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
        user = "ghostnet",
        password = "ghostnet"
)
public class DemoDataInitializer {
    @PersistenceContext(unitName = "ghostNetPU")
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void init() {
        Long count = entityManager.createQuery("select count(g) from GhostNet g", Long.class).getSingleResult();
        if (count > 0) {
            return;
        }

        GhostNet net1 = net(54.8, 10.1, 120.0, "Anonym");
        GhostNet net2 = net(36.9, -5.8, 80.0, "Meldende Person");
        GhostNet net3 = net(-33.9, 18.4, 240.0, "Anonym");
        net3.setStatus(GhostNetStatus.RESCUE_PENDING);
        Rescuer rescuer = new Rescuer();
        rescuer.setName("Beispiel Bergung");
        rescuer.setPhone("+49 170 1234567");
        entityManager.persist(rescuer);
        net3.setRescuer(rescuer);

        entityManager.persist(net1);
        entityManager.persist(net2);
        entityManager.persist(net3);
    }

    private GhostNet net(double lat, double lon, double size, String reporter) {
        GhostNet net = new GhostNet();
        net.setLatitude(lat);
        net.setLongitude(lon);
        net.setEstimatedSizeM2(size);
        net.setReporterName(reporter);
        net.setStatus(GhostNetStatus.REPORTED);
        return net;
    }
}
