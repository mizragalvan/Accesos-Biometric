package mx.engineer.utils.pages;

public final class Pages {
    private Pages() { }
    
    public static Integer getTotalPages(final Integer totalItems, final Integer itemsPerPage) {
        return (int) Math.ceil((double) totalItems / (double) itemsPerPage);
    }
}
