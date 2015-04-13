package com.radek.madej.crm.database.model;

import java.io.Serializable;

public class ModelBase implements Serializable {

   /**
	 * 
	 */
	private static final long serialVersionUID = -3833458765118848477L;
protected long id;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   @Override
   public String toString() {
      return "ModelBase [id=" + this.id + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (this.id ^ (this.id >>> 32));
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof ModelBase)) {
         return false;
      }
      ModelBase other = (ModelBase) obj;
      if (this.id != other.id) {
         return false;
      }
      return true;
   }
}

