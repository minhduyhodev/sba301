package fu.se.chapter13orchid.controllers;

import fu.se.chapter13orchid.pojos.Orchid;
import fu.se.chapter13orchid.services.IOrchidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/orchids")
public class OrchidController {
    @Autowired
    private IOrchidService iOrchidService;

    @GetMapping("/")
    public ResponseEntity<List<Orchid>> fetchAll() {
        return ResponseEntity.ok(iOrchidService.getAllOrchids()); // 200 OK
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Orchid saveOrchid(@RequestBody Orchid orchid) {
        return iOrchidService.insertOrchid(orchid); // 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orchid> updateOrchid(@PathVariable int id, @RequestBody Orchid o) {
        Orchid updateOrchid = iOrchidService.updateOrchid(id, o);
        return ResponseEntity.ok(updateOrchid);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrchid(@PathVariable int id) {
        iOrchidService.deleteOrchid(id);
        return ResponseEntity.ok("Deleted!!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Orchid>> getOById(@PathVariable int id) {
        Optional<Orchid> o = iOrchidService.getOrchidByID(id);
        return ResponseEntity.ok(o); // 200 OK
    }
}
