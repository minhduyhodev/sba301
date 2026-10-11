package fu.se.chapter13orchid.services;

import fu.se.chapter13orchid.pojos.Orchid;

import java.util.List;
import java.util.Optional;

public interface IOrchidService {
    public List<Orchid> getAllOrchids();

    public Orchid insertOrchid(Orchid orchid);

    public Orchid updateOrchid(int orchidID, Orchid orchid);

    public void deleteOrchid(int orchid);

    public Optional<Orchid> getOrchidByID(int orchidID);
}
