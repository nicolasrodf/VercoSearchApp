package com.example.nicolas.vercoapp;

import com.example.nicolas.vercoapp.service.ProductService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Frank on 18/11/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {
    @InjectMocks
    private ProductService productService=new ProductService();
    @Test
    public void getPercentDiscount_isCorrect(){
        assertTrue("Porcentaje en el rango", 0 <= productService.getDiscountPercent(12.345));
    }
    @Test
    public void getDiscountPrice_isCorrect(){
        assertFalse("Descuento menor que el precio", 13.89 > productService.getPriceDiscount(0.002,13.89));
    }
    @Test
    public void getNameFilterType_isCorrect(){
        assertNotNull(productService.getNameFilterType(2));
    }
}
