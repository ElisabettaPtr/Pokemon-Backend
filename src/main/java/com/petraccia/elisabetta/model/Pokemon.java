package com.petraccia.elisabetta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Text;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pokemon {
    private int nationalNumber;
    private String gen;
    private String englishName;
    private String primaryType;
    private String secondaryType;
    private String classification;
    private BigDecimal percentMale;
    private BigDecimal percentFemale;
    private BigDecimal heightM;
    private BigDecimal weightKg;
    private int captureRate;
    private int hp;
    private int attack;
    private int defense;
    private int speed;
    private String abilities0;
    private String abilities1;
    private String abilitiesSpecial;
    private int isSubLegendary;
    private int isLegendary;
    private int isMythical;
    private String evoChain0;
    private String evoChain2;
    private String evoChain4;
    private String megaEvolution;
    private String description;
}


//national_number INT PRIMARY KEY,
//gen VARCHAR(3),
//english_name VARCHAR(255),
//primary_type VARCHAR(255),
//secondary_type VARCHAR(255),
//classification VARCHAR(255),
//percent_male DECIMAL(5,2),
//percent_female DECIMAL(5,2),
//height_m DECIMAL(4,1),
//weight_kg DECIMAL(5,1),
//capture_rate INT,
//hp INT,
//attack INT,
//defense INT,
//speed INT,
//abilities_0 VARCHAR(255),
//abilities_1 VARCHAR(255),
//abilities_special VARCHAR(255),
//is_sublegendary INT,
//is_legendary INT,
//is_mythical INT,
//evochain_0 VARCHAR(255),
//evochain_2 VARCHAR(255),
//evochain_4 VARCHAR(255),
//mega_evolution VARCHAR(255),
//description TEXT