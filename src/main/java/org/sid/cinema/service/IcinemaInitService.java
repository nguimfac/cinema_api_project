package org.sid.cinema.service;

import org.sid.cinema.entities.Categorie;

import java.util.List;

public interface IcinemaInitService {

    public void initVilles();
    public void initCinemas();
    public void initSalles();
    public void initSeances();
    public void initPlaces();
    public void initCategories();
    public void initfilms();
    public void initProjections();
    public   void initTikets();
    public List<Categorie> getCategory();
    public void deleteCategory(Long id);
    public Categorie newCat(Categorie categorie);

}
