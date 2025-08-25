package com.javarush.NWA51.Poltavets.island.service.dto;

public class IslandConfigDTO {
    private final String nameIsland;
    private final int xAxisSize;
    private final int yAxisSize;
    private final int soilType;
    private final int grassGrowthDesert;
    private final int grassGrowthForest;
    private final int grassGrowthSavvanna;
    private final int grassGrowthJungle;
    private final int grassMaxDesert;
    private final int grassMaxForest;
    private final int grassMaxSavvanna;
    private final int grassMaxJungle;

    private IslandConfigDTO(Builder builder){
        this.nameIsland = builder.nameIsland;
        this.xAxisSize = builder.xAxisSize;
        this.yAxisSize = builder.yAxisSize;
        this.soilType = builder.soilType;
        this.grassGrowthDesert = builder.grassGrowthDesert;
        this.grassGrowthForest = builder.grassGrowthForest;
        this.grassGrowthSavvanna = builder.grassGrowthSavvanna;
        this.grassGrowthJungle = builder.grassGrowthJungle;
        this.grassMaxDesert = builder.grassMaxDesert;
        this.grassMaxForest = builder.grassMaxForest;
        this.grassMaxSavvanna = builder.grassMaxSavvanna;
        this.grassMaxJungle = builder.grassMaxJungle;


    }

    public String getNameIsland() {
        return nameIsland;
    }

    public int getxAxisSize() {
        return xAxisSize;
    }

    public int getyAxisSize() {
        return yAxisSize;
    }

    public int getSoilType() {
        return soilType;
    }

    public int getGrassGrowthDesert() {
        return grassGrowthDesert;
    }

    public int getGrassGrowthForest() {
        return grassGrowthForest;
    }

    public int getGrassGrowthSavvanna() {
        return grassGrowthSavvanna;
    }

    public int getGrassGrowthJungle() {
        return grassGrowthJungle;
    }

    public int getGrassMaxDesert() {
        return grassMaxDesert;
    }

    public int getGrassMaxForest() {
        return grassMaxForest;
    }

    public int getGrassMaxSavvanna() {
        return grassMaxSavvanna;
    }

    public int getGrassMaxJungle() {
        return grassMaxJungle;
    }

    // Вложенный статический приватный класс. Каждый метод записвает значение переменной
    // и возвращает сам Builder, чтобы можно было при вызове строить цепочки методов
    public static class Builder {
        private String nameIsland;
        private int xAxisSize;
        private int yAxisSize;
        private int soilType;
        private int grassGrowthDesert;
        private int grassGrowthForest;
        private int grassGrowthSavvanna;
        private int grassGrowthJungle;
        private int grassMaxDesert;
        private int grassMaxForest;
        private int grassMaxSavvanna;
        private int grassMaxJungle;

        public Builder nameIsland(String nameIsland) {
            this.nameIsland = nameIsland;
            return this;
        }

        public Builder xAxisSize(int xAxisSize) {
            this.xAxisSize = xAxisSize;
            return this;
        }

        public Builder yAxisSize(int yAxisSize) {
            this.yAxisSize = yAxisSize;
            return this;
        }

        public Builder soilType(int soilType) {
            this.soilType = soilType;
            return this;
        }

        public Builder grassDesert(int growth, int max) {     //Тут для удобства передаём 2 параметра
            this.grassGrowthDesert = growth;
            this.grassMaxDesert = max;
            return this;
        }

        public Builder grassForest(int growth, int max) {
            this.grassGrowthForest = growth;
            this.grassMaxForest = max;
            return this;
        }

        public Builder grassSavvanna(int growth, int max) {
            this.grassGrowthSavvanna = growth;
            this.grassMaxSavvanna = max;
            return this;
        }

        public Builder grassJungle(int growth, int max) {
            this.grassGrowthJungle = growth;
            this.grassMaxJungle = max;
            return this;
        }

        public IslandConfigDTO build(){
            return new IslandConfigDTO(this);
        }


    }
}



