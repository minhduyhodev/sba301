package fu.se.chapter13orchid.services;

import fu.se.chapter13orchid.pojos.Orchid;
import fu.se.chapter13orchid.repositories.IOrchidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrchidService implements IOrchidService {
    @Autowired
    private IOrchidRepository iOrchidRepository;

    @Override
    public List<Orchid> getAllOrchids() {
        return iOrchidRepository.findAll();
    }

    @Override
    public Orchid insertOrchid(Orchid orchid) {
        return iOrchidRepository.save(orchid);
    }

    @Override
    public Orchid updateOrchid(int orchidID, Orchid orchid) {
        Orchid o = iOrchidRepository.getById(orchidID);
        if (o != null) {
            o.setOrchidName(orchid.getOrchidName());
            o.setOrchidDescription(orchid.getOrchidDescription());
        }
        return null;
    }

    @Override
    public void deleteOrchid(int orchid) {
        iOrchidRepository.deleteById(orchid);
    }

    @Override
    public Optional<Orchid> getOrchidByID(int orchidID) {
        return iOrchidRepository.findById(orchidID);
    }
}
