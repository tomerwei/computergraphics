package smarthomevis.architecture.logic;

import smarthomevis.architecture.persistence.Repository;

public interface Controller<E> {

    E get(String objectId);

    String save(String name);

    void delete(String objectId);

    Repository getRepository();

}
