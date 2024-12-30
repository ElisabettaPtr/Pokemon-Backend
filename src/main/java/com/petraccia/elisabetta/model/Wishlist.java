package com.petraccia.elisabetta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {
    private int idWishlist;
    private int idUser;
    private int nationalNumber;
}
