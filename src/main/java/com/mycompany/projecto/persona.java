package com.mycompany.projecto;

public class persona {
         private String name;
         private String DNI;
         
  
        public persona(String name, String DNI) {
            this.name = name;
            this.DNI = DNI;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDNI() {
            return DNI;
        }

        public void setDNI(String DNI) {
            this.DNI = DNI;
        }
    }