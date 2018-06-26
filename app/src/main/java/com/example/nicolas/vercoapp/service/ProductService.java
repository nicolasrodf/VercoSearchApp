package com.example.nicolas.vercoapp.service;

/**
 * Created by Frank on 18/11/2017.
 */

public class ProductService {
    public Integer getDiscountPercent(Double discount){
        return (int)(discount*100);
    }
    public Double getPriceDiscount(Double discountPercent, Double price){
        return (double)Math.round(price*(1 - discountPercent));
    }
    public String getNameFilterType(int filterType){
        System.out.print("Tipo de Filtro"+filterType);
        String filterString="";
        switch (filterType) {
            case 0:
                filterString="hombre";
                break;
            case 1:
                filterString="mujer";
                break;
            case 2:
                filterString="mostrar todo";
                break;
        }
        return filterString;
    }
}
