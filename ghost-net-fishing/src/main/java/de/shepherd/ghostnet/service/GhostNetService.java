package de.shepherd.ghostnet.service;

import de.shepherd.ghostnet.entity.GhostNet;
import de.shepherd.ghostnet.entity.GhostNetStatus;
import de.shepherd.ghostnet.entity.Rescuer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class GhostNetService {
    @PersistenceContext(unitName = "ghostNetPU")
    private EntityManager entityManager;

    public List<GhostNet> findAll() {
        return entityManager.createQuery("select g from GhostNet g order by g.id desc", GhostNet.class)
                .getResultList();
    }

    public List<GhostNet> findOpen() {
        return entityManager.createQuery(
                "select g from GhostNet g where g.status in :statuses order by g.id desc", GhostNet.class)
                .setParameter("statuses", List.of(GhostNetStatus.REPORTED, GhostNetStatus.RESCUE_PENDING))
                .getResultList();
    }

    public List<GhostNet> findAssignedOpen() {
        return entityManager.createQuery(
                "select g from GhostNet g where g.status = :status and g.rescuer is not null order by g.id desc", GhostNet.class)
                .setParameter("status", GhostNetStatus.RESCUE_PENDING)
                .getResultList();
    }

    public List<GhostNet> findPendingFor(Rescuer rescuer) {
        return entityManager.createQuery(
                "select g from GhostNet g where g.rescuer = :rescuer and g.status = :status order by g.id desc", GhostNet.class)
                .setParameter("rescuer", rescuer)
                .setParameter("status", GhostNetStatus.RESCUE_PENDING)
                .getResultList();
    }

    @Transactional
    public void save(GhostNet ghostNet) {
        entityManager.persist(ghostNet);
    }

    @Transactional
    public boolean claim(Long ghostNetId, String name, String phone) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);
        if (ghostNet == null || ghostNet.getStatus() != GhostNetStatus.REPORTED || ghostNet.getRescuer() != null) {
            return false;
        }
        Rescuer rescuer = findOrCreateRescuer(name, phone);
        ghostNet.setRescuer(rescuer);
        ghostNet.setStatus(GhostNetStatus.RESCUE_PENDING);
        return true;
    }

    @Transactional
    public boolean markRecovered(Long ghostNetId, String name, String phone) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);
        if (ghostNet == null || ghostNet.getStatus() != GhostNetStatus.RESCUE_PENDING || ghostNet.getRescuer() == null) {
            return false;
        }
        if (!matches(ghostNet.getRescuer(), name, phone)) {
            return false;
        }
        ghostNet.setStatus(GhostNetStatus.RECOVERED);
        return true;
    }

    @Transactional
    public boolean markLost(Long ghostNetId, String name, String phone) {
        GhostNet ghostNet = entityManager.find(GhostNet.class, ghostNetId);
        if (ghostNet == null || ghostNet.getStatus() == GhostNetStatus.RECOVERED || ghostNet.getStatus() == GhostNetStatus.LOST) {
            return false;
        }
        if (isBlank(name) || isBlank(phone)) {
            return false;
        }
        ghostNet.setStatus(GhostNetStatus.LOST);
        return true;
    }

    private Rescuer findOrCreateRescuer(String name, String phone) {
        List<Rescuer> matches = entityManager.createQuery(
                "select r from Rescuer r where r.name = :name and r.phone = :phone", Rescuer.class)
                .setParameter("name", name)
                .setParameter("phone", phone)
                .setMaxResults(1)
                .getResultList();
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        Rescuer rescuer = new Rescuer();
        rescuer.setName(name);
        rescuer.setPhone(phone);
        entityManager.persist(rescuer);
        return rescuer;
    }

    private boolean matches(Rescuer rescuer, String name, String phone) {
        return rescuer.getName().equals(name) && rescuer.getPhone().equals(phone);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
