package fu.edu.sba301.assignment2.dto;

public class CategoryResponse {
    private Integer categoryId;
    private String categoryName;
    private String categoryDesciption;
    private Integer parentCategoryId;
    private Boolean isActive;

    public CategoryResponse() {
    }

    public CategoryResponse(Integer categoryId, String categoryName, String categoryDesciption, Integer parentCategoryId, Boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDesciption = categoryDesciption;
        this.parentCategoryId = parentCategoryId;
        this.isActive = isActive;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesciption() {
        return categoryDesciption;
    }

    public void setCategoryDesciption(String categoryDesciption) {
        this.categoryDesciption = categoryDesciption;
    }

    // Helper alias
    public String getCategoryDescription() {
        return categoryDesciption;
    }

    public void setCategoryDescription(String description) {
        this.categoryDesciption = description;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
