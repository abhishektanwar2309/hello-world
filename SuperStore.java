import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
//////////////////////////////////////////
public class SuperStore {
    //contains the map of warehouses linked to it
    //map is of the key of the warehouses and warehouse
    private HashMap<Integer,Warehouse> allWarehouses;
    private HashMap<Integer,Store> allStores;
    private HashMap<Integer,User> allUsers;
    private SuperUser superAdmin;
    private Category Inventory;
    //constructor
    public SuperStore(){
        this.allWarehouses=new HashMap<>();
        this.allStores=new HashMap<>();
        this.allUsers=new HashMap<>();
        this.superAdmin=new SuperUser("superAdmin");
        this.Inventory=new Category("SuperCategory");
    }
    //-----------------Function to check the type of User -----------------------------------------------------------//
    //function to check if the user is super user by only using it's id , password can be added as well
    public boolean checkIfSuperuser(int idOfUser){
        User original=this.getAllUsers().get(idOfUser);
        if (this.superAdmin.getClass().equals(original.getClass())){
            return true;
        }
        return false;
    }
    //---------------------------Functions to manage Categories(Items) ----------------------------------------------//
    public void displayCategories(){
        displayCategoriesHelper(this.Inventory);
    }
    private static void displayCategoriesHelper(Category root){
        Queue<Category> pending=new LinkedList<>();
        ((LinkedList<Category>) pending).add(root);
        while (!pending.isEmpty()){
            Category currentCategory=pending.poll();
            //printing the name of the main class first
            System.out.print(currentCategory.getName()+" : ");
            //now printing its sublists and also adding them in the queue
            //iterating over the sublist of the currentlist
            for (Category i:currentCategory.getSubCategory()){
                System.out.print(i.getName()+" , ");
                pending.add(i);
            }
            System.out.println();
        }
    }
    //Function to search a category and return it if found otherwise null is returned
    public Category searchCategory(String nameCategory){
        return  searchCategoryHelper(Inventory,nameCategory);
    }
    private static Category searchCategoryHelper(Category root,String nameCategory){
        if (root.getName().equals(nameCategory)){
            return root;
        }
        //else starting earch it in every child subtree using recursion
        Category check=null;
        for (Category i:root.getSubCategory()){
            check=searchCategoryHelper(i,nameCategory);
            if (check!=null){
                break;
            }
        }
        return check;
    }
    public void addCategory(String path,String newCategory) throws AlreadyPresentException{
        String pathArray[]=path.split(">");
        Category newcat=new Category(newCategory);
        if (searchCategory(newCategory)==null){
            addCategoryHelper(Inventory,pathArray,newCategory,0);
        }
        else{
            //throw an exception here please
            throw new AlreadyPresentException();
        }
    }
    public void addCategoryHelper(Category root,String path[],String newCategory,int i){
        if (i==path.length){
            //adding it to the children of the root
            Category newcat=new Category(newCategory);
            root.getSubCategory().add(newcat);
            return;
        }
        //now otherwise we have to first get the categpry at i
        Category newroot=null;
        if (searchCategory(path[i])==null){
            Category present=new Category(path[i]);
            root.getSubCategory().add(present);
            newroot=present;
        }
        else{
            newroot=searchCategory(path[i]);
        }
        addCategoryHelper(newroot,path,newCategory,i+1);
    }
    //function to delete a category
    public void deleteCategory(String path){
        String inputPath[]=path.split(">");
        try {
            this.deleteCategoryHelper(this.Inventory, inputPath, 0);
        }catch (NotPresentException e){
            System.out.println("CategoryNotPresentException");
        }
    }
    private void deleteCategoryHelper(Category mainRoot,String[] inputPath,int i) throws NotPresentException{
        if (i==inputPath.length-1){
            if (this.searchCategory(inputPath[i])!=null){
                //getting the index of inputPath[i] in subcategory list of mainRoot
                int index=0;
                for (int j=0;j<mainRoot.getSubCategory().size();j++){
                    if ((mainRoot.getSubCategory().get(j).getName()).equals(inputPath[i])) {
                        index=j;
                        break;
                    }
                }
                //removing the element at index "index" from the subcategory list of mainRoot
                mainRoot.getSubCategory().remove(index);
            }
            else{
                System.out.println("Category not present");
            }
            return;
        }
        if (this.searchCategory(inputPath[i])!=null){
            System.out.println("Invalid Path");
            return;
        }
        Category newRoot=searchCategory(inputPath[i]);
        deleteCategoryHelper(newRoot,inputPath,i+1);
    }

    //---------------------------------------------------------------------------------------------------------------//
    public HashMap<Integer, Warehouse> getAllWarehouses() {
        return allWarehouses;
    }

    public void setAllWarehouses(HashMap<Integer, Warehouse> allWarehouses) {
        this.allWarehouses = allWarehouses;
    }

    public HashMap<Integer, Store> getAllStores() {
        return allStores;
    }

    public void setAllStores(HashMap<Integer, Store> allStores) {
        this.allStores = allStores;
    }

    public HashMap<Integer, User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(HashMap<Integer, User> allUsers) {
        this.allUsers = allUsers;
    }

    public SuperUser getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(SuperUser superAdmin) {
        this.superAdmin = superAdmin;
    }

    public Category getInventory() {
        return Inventory;
    }

    public void setInventory(Category inventory) {
        Inventory = inventory;
    }
    public static void main(String args[]) {
        //making a stupid tree

        SuperStore obj = new SuperStore();
        try {
            obj.addCategory("clothing>shirts", "purpleshirt");
            obj.addCategory("watches>rado", "copperBlack");
            obj.displayCategories();
            System.out.println("------------------------");
            obj.addCategory("clothing>shirts", "RedOnes");
            obj.displayCategories();
            System.out.println("------------------------");
            obj.addCategory("electronics>trimmers>philips", "razorsharp");
            obj.displayCategories();
            System.out.println("------------------------lllllllllllllllllll");
            obj.deleteCategory("watches");
            obj.deleteCategory("socks");
            obj.displayCategories();
            System.out.println("------------------------");
            obj.addCategory("watches", "rado");
            obj.displayCategories();
        } catch (AlreadyPresentException e) {
            System.out.println("AlreadyPresentException");
        }
    }
}
