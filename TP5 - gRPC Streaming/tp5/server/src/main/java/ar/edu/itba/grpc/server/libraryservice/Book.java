package ar.edu.itba.grpc.server.libraryservice;

public class Book {
    private int amount;
    private String isbn;
    private String title;
    private String releaseDate;
    private String authorLastName;
    private String authorFirstName;
    public Book(int amount, String isbn, String title, String releaseDate, String authorLastName, String authorFirstName) {
        this.amount = amount;
        this.isbn = isbn;
        this.title = title;
        this.releaseDate = releaseDate;
        this.authorLastName = authorLastName;
        this.authorFirstName = authorFirstName;
    }
    public Boolean lendBook(){
        if(amount > 0){
            amount--;
            return true;
        }
        return false;
    }
    public void returnBook(){
        amount++;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getAuthorLastName() {
        return authorLastName;
    }
    public String getAuthorFirstName() {
        return authorFirstName;
    }

}
