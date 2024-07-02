package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.SimpleResponse;
import peaksoft.dto.dtoCategory.CategoryRequest;
import peaksoft.dto.dtoCategory.CategoryResponse;
import peaksoft.dto.dtoCategory.PaginationResponse;
import peaksoft.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryAPI {
    private final CategoryService service;

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF','WAITER')")
    @GetMapping("/getAllCategories")
    public PaginationResponse getAllCategories(@RequestParam int currentPage,@RequestParam int pageSize){
        return service.getAllCategories(currentPage, pageSize);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/saveCategory")
    public SimpleResponse saveCategory(@RequestBody CategoryRequest categoryRequest){
        return service.saveCategory(categoryRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/updateById/{id}")
    public SimpleResponse updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest){
        return service.updateCategory(id,categoryRequest);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF','WAITER')")
    @GetMapping("/getById/{id}")
    public  CategoryResponse getCategoryById(@PathVariable Long id){
        return service.getCategoryById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF','WAITER')")
    @DeleteMapping("deleteById/{id}")
    public SimpleResponse deleteCategory(@PathVariable Long id){
        return service.deleteCategoryById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF','WAITER')")
    @GetMapping("/search")
    public PaginationResponse search(@RequestParam String word,@RequestParam int currentPage,@RequestParam int pageSize){
        return service.searchCategoryByName(word, currentPage, pageSize);
    }
}
