package com.faehpremium.numisperu.model;

/**
 * Clase modelo para una colección o álbum numismático peruano.
 */
public class CoinCollection {
    private long id;
    private String name;
    private int startYear;
    private int endYear;
    private String description;
    private String category;
    private boolean isCustom;
    private int sortOrder;
    private String mintMask;

    public CoinCollection() {
        this.category = "Series Regulares";
        this.mintMask = "Lma";
        this.sortOrder = 0;
    }

    public CoinCollection(String name, int startYear, int endYear, String description, String category, boolean isCustom) {
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
        this.description = description;
        this.category = category;
        this.isCustom = isCustom;
        this.sortOrder = 0;
        this.mintMask = "Lma";
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStartYear() { return startYear; }
    public void setStartYear(int startYear) { this.startYear = startYear; }

    public int getEndYear() { return endYear; }
    public void setEndYear(int endYear) { this.endYear = endYear; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isCustom() { return isCustom; }
    public void setCustom(boolean custom) { isCustom = custom; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public String getMintMask() { return mintMask; }
    public void setMintMask(String mintMask) { this.mintMask = mintMask; }
}
