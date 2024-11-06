package dto;

import java.util.Arrays;

public class Courier {
        private String login;
        private String password;
        private String firstName;

        public Courier(String login, String password, String firstName){
            this.login = login;
            this.password = password;
            this.firstName = firstName;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString(){
            return "Courier{" +
                    "login=" + login +
                    ", password=" + password +
                    ", firstName='" + firstName +
                    "}";
        }


}
