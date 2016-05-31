import java.util.*;

interface Executor <Number> {
// Добавить таск на выполнение. Результат таска будет доступен через метод getValidResults(). 
// Бросает Эксепшн если уже был вызван метод execute()
        void addTask(Task task);
 // Добавить таск на выполнение и валидатор результата. Результат таска будет записан в ValidResults если validator.isValid вернет true для этого результата
 // Результат таска будет записан в InvalidResults если validator.isValid вернет false для этого результата
 // Бросает Эксепшн если уже был вызван метод execute()
        void addTask(Task task, Validator validator);
 // Выполнить все добавленые таски
        void execute();
 // Получить валидные результаты. Бросает Эксепшн если не был вызван метод execute()
        List getValidResults();
 // Получить невалидные результаты. Бросает Эксепшн если не был вызван метод execute()
        List getInvalidResults();
}

interface Task <Number> {
        // Метода запускает таск на выполнение
        void execute();
        // Возвращает результат выполнения
        Object getResult();
}
interface Validator <Number> {
        // Валидирует переданое значение
        boolean isValid(Object result);
}
////////
class NumberValidator implements Validator
{
 public boolean isValid(Object result)
 {  
   if (result instanceof Number) return true;
   return false; }
}

class ExecException extends Exception 
{ 
public void print()
{ System.out.println("Tasks are added after their executed!");  }
}


class ExecutorImpl<Number> implements Executor
{
    private static int _execute = 0;
    private List<Task> lt=new ArrayList<>();
    private List<NumberValidator> lv=new ArrayList<>();
    private ArrayList<Integer> vr= new ArrayList<Integer>();
    private ArrayList<Integer> ivr= new ArrayList<Integer>();

        public void addTask(Task task) 
         {
         try {
          if (_execute!=0) throw new ExecException(); 
          lt.add(task); lv.add(null); 
          }
         catch(ExecException e) {e.print();} 
        }

        public void addTask(Task task, Validator validator) 
         {
         try {
          if (_execute!=0) throw new ExecException(); 
          lt.add(task); lv.add((NumberValidator)validator); 
          }
         catch(ExecException e) {e.print();} 
        }

        public void execute() 
        {
         try {
          if (_execute!=0) throw new ExecException(); 
          _execute++;
          System.out.println("Tasks are executing");
          NumberValidator v;
          int i=0;
          for (Task t:lt) 
           {t.execute(); v=lv.get(i);
            if (v==null) vr.add(i);  
            if ((v!=null)&&(v.isValid(t.getResult()))) vr.add(i);
            if ((v!=null)&&(!v.isValid(t.getResult()))) ivr.add(i);
            i++;
           }
         }
         catch(ExecException e) {System.out.println("Tasks were executed");} 
        }

        public List<Integer> getValidResults() 
         {//System.out.println("Task ValidRes");
            return vr;
//          return (new ArrayList<Integer>(Arrays.asList(new Integer[]{1,2,3})));
         }
        public List<Integer> getInvalidResults() 
         {//System.out.println("Task InvalidRes");
            return ivr;
//          return (new ArrayList<Integer>(Arrays.asList(new Integer[]{11,12,13})));
         }
}
class LongTask implements Task
{
 long k;
 Long res;
 public LongTask(Long n)
 {
  k=n;
 }
 public void execute() {System.out.println("LongTask ("+k+")"); res=2*k; }
 public Long getResult() {return res;}
}

class StringTask implements Task
{
 String s;
 String res;
 public StringTask(String p)
 {
  s=p;
 }
 public void execute() {System.out.println("StringTask ("+s+")"); res=s+s; }
 public String getResult() {return res;}
}

////////

class Tests {  

public void test(List<Task<Integer>> intTasks) {
        Executor<Number> numberExecutor = new ExecutorImpl<Integer>();
        for (Task<Integer> intTask : intTasks) {
            numberExecutor.addTask(intTask);
        }
        numberExecutor.addTask(new LongTask(10L), new NumberValidator());
        numberExecutor.addTask(new StringTask("Str2"), new NumberValidator());
        numberExecutor.execute();
        if (numberExecutor.getValidResults().size()==0) System.out.println("No Valid results.");
        else 
         {
         System.out.println("Valid results:");
         for (Number number : (ArrayList<Number>) numberExecutor.getValidResults()) 
            System.out.println(number);
         }
        if (numberExecutor.getInvalidResults().size()==0) System.out.println("No Invalid results.");
        else 
         {
         System.out.println("Invalid results:");
         for (Number number : (ArrayList<Number>) numberExecutor.getInvalidResults()) 
            System.out.println(number);
         }
}

  public static void main(String[] args) 
    {
        List<Task<Integer>> t = new ArrayList<>();
        LongTask l= new LongTask(-1L);
        t.add(l); 
        StringTask s= new StringTask("Str1");
        t.add(s); 
        Tests x= new Tests();
        x.test(t);
        System.out.println("End Of 1Test");
        x.test(t);
        System.out.println("End Of 2Test");
    }
}

