package pe.edu.upc.fixcampus.service;
import pe.edu.upc.fixcampus.dto.Dtos;
import static pe.edu.upc.fixcampus.dto.Dtos.*;
import pe.edu.upc.fixcampus.event.*;
import pe.edu.upc.fixcampus.exception.*;
import pe.edu.upc.fixcampus.entities.*;
import pe.edu.upc.fixcampus.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CatalogService {
    private final CategoryRepository categories;
    private final AreaRepository areas;
    private final TechnicianRepository technicians;
    private final UserRepository users;
    private final CurrentUser current;
    public CatalogService(CategoryRepository categories,AreaRepository areas,TechnicianRepository technicians,UserRepository users,CurrentUser current) {
        this.categories=categories; this.areas=areas; this.technicians=technicians; this.users=users; this.current=current;
    }
    public List<CatalogView> categories() { current.get(); return categories.findAll().stream().map(CatalogService::view).toList(); }
    public List<CatalogView> areas() { current.get(); return areas.findAll().stream().map(CatalogService::view).toList(); }
    public List<TechnicianView> technicians() { current.get(); return technicians.findAll().stream().map(CatalogService::view).toList(); }
    public CatalogView category(Long id) { current.get(); return view(findCategory(id)); }
    public CatalogView area(Long id) { current.get(); return view(findArea(id)); }
    public TechnicianView technician(Long id) { current.get(); return view(findTechnician(id)); }
    public CatalogView saveCategory(Long id,CatalogInput input) {
        current.admin();
        var entity=id==null ? new Category() : findCategory(id);
        entity.name=input.name().trim(); entity.description=input.description(); entity.active=input.active();
        return view(categories.saveAndFlush(entity));
    }
    public CatalogView saveArea(Long id,CatalogInput input) {
        current.admin();
        var entity=id==null ? new Area() : findArea(id);
        entity.name=input.name().trim(); entity.description=input.description(); entity.active=input.active();
        return view(areas.saveAndFlush(entity));
    }
    public TechnicianView saveTechnician(Long id,TechnicianInput input) {
        current.admin();
        AppUser user=users.findById(input.userId()).orElseThrow(ApiException::missing);
        if(user.role!=Role.TECHNICIAN || !user.active) throw new ApiException(400,"technicianRole");
        var entity=id==null ? new Technician() : findTechnician(id);
        if(id!=null && !entity.user.id.equals(user.id)) throw new ApiException(409,"technicianLinked");
        entity.user=user; entity.specialty=input.specialty().trim(); entity.active=input.active();
        return view(technicians.saveAndFlush(entity));
    }
    public void disableCategory(Long id) { current.admin(); findCategory(id).active=false; }
    public void disableArea(Long id) { current.admin(); findArea(id).active=false; }
    public void disableTechnician(Long id) { current.admin(); findTechnician(id).active=false; }
    Category findCategory(Long id) { return categories.findById(id).orElseThrow(ApiException::missing); }
    Area findArea(Long id) { return areas.findById(id).orElseThrow(ApiException::missing); }
    Technician findTechnician(Long id) { return technicians.findById(id).orElseThrow(ApiException::missing); }
    static CatalogView view(Category value) { return new CatalogView(value.id,value.name,value.description,value.active); }
    static CatalogView view(Area value) { return new CatalogView(value.id,value.name,value.description,value.active); }
    static TechnicianView view(Technician value) { return new TechnicianView(value.id,value.user.id,value.user.name,value.user.email,value.specialty,value.active && value.user.active); }
}
