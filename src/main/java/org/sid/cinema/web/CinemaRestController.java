package org.sid.cinema.web;

import lombok.Data;
import org.sid.cinema.dao.FilmRepository;
import org.sid.cinema.dao.TicketRepository;
import org.sid.cinema.entities.Categorie;
import org.sid.cinema.entities.Film;
import org.sid.cinema.entities.Ticket;
import org.sid.cinema.service.CinemaInitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;


@RestController
@CrossOrigin("*") //pour autoriser la partie front-end a acceder au donnes
public class CinemaRestController {
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CinemaInitServiceImpl cinemaInitService;

    @GetMapping("/listFilms")
    public List<Film> films()
    {
        return filmRepository.findAll();
    }

    @GetMapping(path = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id")Long id) throws Exception{
       Film f= filmRepository.findById(id).get();
        String photoName = f.getPhoto();
        File file = new File(System.getProperty("user.home")+"/cinema/images/"+photoName);
        Path path = Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }
    
    @PostMapping("/payerTickets")
    @Transactional
    public List<Ticket> payerTickets(@RequestBody TicketFrom ticketFrom){
        List<Ticket> listTickets = new ArrayList<>();
            ticketFrom.getTickets().forEach(idTicket->{
            Ticket ticket =ticketRepository.findById(idTicket).get();
            ticket.setNomClient(ticketFrom.getNomClient());
            ticket.setReserve(true);
            ticket.setCodePayement(ticketFrom.getCodePayement());
            ticketRepository.save(ticket);
            listTickets.add(ticket);
        });
            return listTickets;
    }


    @GetMapping("/AllCategory")
    public ResponseEntity<List<Categorie>> getAllCat(){
        List<Categorie> categories = cinemaInitService.getCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @DeleteMapping("deleteCat/{id}")
    public ResponseEntity<Categorie> deleteCategory(@PathVariable("id")Long id)
    {
        cinemaInitService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/newCate")
    public ResponseEntity<Categorie> newCateg(@RequestBody Categorie  categorie)
    {
        cinemaInitService.newCat(categorie);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}

@Data
class TicketFrom{
    private String nomClient;
    private int codePayement;
    private List<Long> tickets = new ArrayList<>();
}


