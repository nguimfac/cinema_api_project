package org.sid.cinema.service;

import org.sid.cinema.dao.*;
import org.sid.cinema.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class CinemaInitServiceImpl implements IcinemaInitService{

    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionRepository projectionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("douala","Yaounde","Bafoussam","Edea").forEach(nameVille->{
            Ville ville= new Ville();
            ville.setName(nameVille);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
      villeRepository.findAll().forEach(v->{
          Stream.of("MegaRama","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ")
          .forEach(nameCinema->{
              Cinema cinema= new Cinema();
              cinema.setName(nameCinema);
              cinema.setNombreSalles(3+(int) (Math.random()*7));
              cinema.setVille(v);
              cinemaRepository.save(cinema);
          });
      });

    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(cinema -> {
            for (int i=0;i<cinema.getNombreSalles();i++) {
                Salle salle =new Salle();
                salle.setName("Salle"+(i+1));
                salle.setCinema(cinema);
                salle.setNombrePlace(15+(int)(Math.random()*10));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initSeances() {

        DateFormat dateFormat  = new SimpleDateFormat("HH:mm");
        Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(dateFormat.parse(s));
                 seanceRepository.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void initPlaces() {
      salleRepository.findAll().forEach(salle -> {
          for (int i=0 ;i<salle.getNombrePlace();i++){
              Place place = new Place();
              place.setNumero(i+1);
              place.setSalle(salle);
              placeRepository.save(place);
          }
      });
    }

    @Override
    public void initCategories() {

        Stream.of("Histoire","Action","Fiction","Drama").forEach(cat->{
            Categorie categorie=new Categorie();
            categorie.setName(cat);
            categoryRepository.save(categorie);
        });
    }

    @Override
    public void initfilms() {
        double[] durees=new double[]{1,1.5,2,2.5,3};
        List<Categorie>  categories =categoryRepository.findAll();
        Stream.of("Game  of trones","Seigneur des Anneaux","Spiderman","Iron man","Cat woman")
                .forEach(titreFilm->{
                    Film film =new Film();
                    film.setTitre(titreFilm);
                    film.setDuree(durees[new Random().nextInt(durees.length)]);
                    film.setPhoto(titreFilm.replaceAll(" ","")+".jpg");
                    film.setCategorie(categories.get(new Random().nextInt(categories.size())));
                    filmRepository.save(film);
                });
    }

    @Override
    public void initProjections() {
      double[] prices =new double[]{30,50,60,70,90,100};
      List<Film> films = filmRepository.findAll();
        villeRepository.findAll().forEach(ville -> {
            ville.getCinemas().forEach(cinema -> {
                cinema.getSalles().forEach(salle -> {
                    int index = new Random().nextInt(films.size());
                    Film film = films.get(index);
                        seanceRepository.findAll().forEach(seance -> {
                            Projection projection = new Projection();
                            projection.setDateProjection(new Date());
                            projection.setFilm((film));
                            projection.setPrix(prices[new Random().nextInt(prices.length)]);
                            projection.setSalle(salle);
                            projection.setSeance(seance);
                            projectionRepository.save(projection);
                        });
                    });
                });
            });
    }

    @Override
    public void initTikets() {
     projectionRepository.findAll().forEach(p->{
            p.getSalle().getPlaces().forEach(place -> {
                Ticket ticket=new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(p.getPrix());
                ticket.setProjection(p);
                ticket.setReserve(false);
                ticketRepository.save(ticket);
            });
        });
    }

    @Override
    public List<Categorie> getCategory() {
        List<Categorie> categories=new ArrayList<>();
        categoryRepository.findAll().forEach(categories::add);
        return categories;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Categorie newCat(Categorie categorie) {
       return categoryRepository.save(categorie);
    }
}
