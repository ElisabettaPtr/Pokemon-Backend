package com.petraccia.elisabetta.service;

import com.petraccia.elisabetta.dao.WishlistDAO;
import com.petraccia.elisabetta.model.Wishlist;

import java.util.List;
import java.util.Map;

public class WishlistService {
    private final WishlistDAO wishlistDAO = new WishlistDAO();

    public Wishlist addToWishlist(Wishlist wishlist) {
        return wishlistDAO.addToWishlist(wishlist);
    }

    public List<Map<String, Object>> getWishlistWithDetailsByUserId(int userId) {
        return wishlistDAO.getWishlistWithDetailsByUserId(userId);
    }

    public void deleteFromWishlist(int idWishlist) {
        wishlistDAO.deleteFromWishlist(idWishlist);
    }
}
