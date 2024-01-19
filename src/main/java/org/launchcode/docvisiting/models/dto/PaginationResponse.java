package org.launchcode.docvisiting.models.dto;


import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationResponse {

     private Page page;
     private int currentPage;
    private long totalElements;
    private int totalPages;
    public PaginationResponse(Page page, int currentPage) {
        this.page = page;
        this.currentPage = currentPage;
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public PaginationResponse() {

    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public int getNextPage() {
        return Math.min(this.currentPage + 1,totalPages);
    }

    public int getPrevPage() {
        return Math.max(this.currentPage - 1, 0);
    }

    public boolean isFirstPage(){
        return (this.currentPage == 0);
    }

    public boolean isLastPage(){
        return (this.currentPage + 1 == this.totalPages);
    }

    public List getResults(){
        return this.page.getContent();
    }

    public String lastPageClass (){
        if(isLastPage()){
            return "page-item disabled";
        } else {
            return "page-item";
        }
    }
    public String firstPageClass (){
        if(isFirstPage()){
            return "page-item disabled";
        } else {
            return "page-item";
        }
    }
}
