package com.faehpremium.numisperu.model;

/**
 * Clase modelo para una casilla de moneda individual dentro de una colección numismática.
 * Mantiene información detallada de posesión, cantidad, grado numismático y notas.
 */
public class CoinSlot {
    private long id;
    private long collectionId;
    private String coinIdentifier;
    private int year;
    private String mint;
    private boolean isOwned;
    private int quantity;
    private String grade;
    private String notes;
    private String label;
    private String finishType;
    private boolean isCustomCoin;
    private int sortOrder;

    public CoinSlot() {
        this.mint = "Lma";
        this.quantity = 0;
        this.grade = "Sin especificar";
        this.notes = "";
        this.finishType = "Circulación";
        this.isCustomCoin = false;
        this.sortOrder = 0;
    }

    public CoinSlot(long collectionId, String label, int year, String mint, boolean isOwned, int quantity) {
        this();
        this.collectionId = collectionId;
        this.label = label;
        this.year = year;
        this.mint = mint;
        this.isOwned = isOwned;
        this.quantity = quantity;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getCollectionId() { return collectionId; }
    public void setCollectionId(long collectionId) { this.collectionId = collectionId; }

    public String getCoinIdentifier() { return coinIdentifier; }
    public void setCoinIdentifier(String coinIdentifier) { this.coinIdentifier = coinIdentifier; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getMint() { return mint; }
    public void setMint(String mint) { this.mint = mint; }

    public boolean isOwned() { return isOwned; }
    public void setOwned(boolean owned) { this.isOwned = owned; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getFinishType() { return finishType; }
    public void setFinishType(String finishType) { this.finishType = finishType; }

    public boolean isCustomCoin() { return isCustomCoin; }
    public void setCustomCoin(boolean customCoin) { isCustomCoin = customCoin; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
