package com.extraspellattributes.interfaces;

import com.extraspellattributes.api.RecoupInstances;

import java.util.List;

public interface RecoupLivingEntityInterface {
    List<RecoupInstances.RecoupInstanceHealth> getRecoupsHealth();
    List<RecoupInstances.RecoupInstanceAbsorption> getRecoupsAbsorption();

    void tickRecoups();
    void addRecoupHealth(RecoupInstances.RecoupInstanceHealth instance);
    void addRecoupAbsorption(RecoupInstances.RecoupInstanceAbsorption instance);

}
