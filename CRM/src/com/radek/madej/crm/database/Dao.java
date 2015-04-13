package com.radek.madej.crm.database;

import java.util.List;

public interface Dao<T> {

   long save(T type);

   int update(T type);

   int delete(T type);

   T get(long id);

   List<T> getAll();

}
