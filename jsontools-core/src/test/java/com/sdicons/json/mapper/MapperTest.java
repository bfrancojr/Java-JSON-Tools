package com.sdicons.json.mapper;

import com.sdicons.json.mapper.helper.impl.DateMapper;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.helper.JSONMap;
import com.sdicons.json.helper.JSONConstruct;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MapperTest
extends TestCase
{
    public static enum TheSimpsons {HOMER, BART, LISA, MARGE, MAGGY};

    public static class TestBean
    {
        private Boolean booleanMbr;
        private String stringMbr;
        private Integer integerMbr;
        private int intMbr;

        private Short shortMbr;
        private Byte byteMbr;
        private Long longMbr;
        private Float floatMbr;
        private Double doubleMbr;
        private BigInteger bigIntMbr;
        private BigDecimal bigDecimalMbr;
        private Character charMbr;
        private Date date;
        private Boolean true1;
        private Boolean true2;
        private Boolean true3;
        private Boolean false1;
        private Boolean false2;
        private Boolean false3;
        private String onlyReadableProperty="read me";
        private String onlyWritableProperty;
        private LinkedList<String> linkedList;
        private ArrayList<Date> arrayList;
        private TheSimpsons simpson;

        private LinkedHashMap<String, Date> linkedMap;

        public Integer getIntegerMbr()
        {
            return integerMbr;
        }

        public void setIntegerMbr(Integer integerMbr)
        {
            this.integerMbr = integerMbr;
        }

		public void setOnlyWritableProperty(String onlyWritableProperty) {
			this.onlyWritableProperty = onlyWritableProperty;
		}

		public String getOnlyReadableProperty() {
			return onlyReadableProperty;
		}

		public Short getShortMbr()
        {
            return shortMbr;
        }

        public void setShortMbr(Short shortMbr)
        {
            this.shortMbr = shortMbr;
        }

        public String getStringMbr()
        {
            return stringMbr;
        }

        public void setStringMbr(String stringMbr)
        {
            this.stringMbr = stringMbr;
        }

        public Boolean getBooleanMbr()
        {
            return booleanMbr;
        }

        public void setBooleanMbr(Boolean booleanMbr)
        {
            this.booleanMbr = booleanMbr;
        }

        public Byte getByteMbr()
        {
            return byteMbr;
        }

        public void setByteMbr(Byte byteMbr)
        {
            this.byteMbr = byteMbr;
        }

        public Long getLongMbr()
        {
            return longMbr;
        }

        public void setLongMbr(Long longMbr)
        {
            this.longMbr = longMbr;
        }

        public Double getDoubleMbr()
        {
            return doubleMbr;
        }

        public void setDoubleMbr(Double doubleMbr)
        {
            this.doubleMbr = doubleMbr;
        }

        public Float getFloatMbr()
        {
            return floatMbr;
        }

        public void setFloatMbr(Float floatMbr)
        {
            this.floatMbr = floatMbr;
        }

        public BigDecimal getBigDecimalMbr()
        {
            return bigDecimalMbr;
        }

        public void setBigDecimalMbr(BigDecimal bigDecimalMbr)
        {
            this.bigDecimalMbr = bigDecimalMbr;
        }

        public BigInteger getBigIntMbr()
        {
            return bigIntMbr;
        }

        public void setBigIntMbr(BigInteger bigIntMbr)
        {
            this.bigIntMbr = bigIntMbr;
        }

        public Character getCharMbr()
        {
            return charMbr;
        }

        public void setCharMbr(Character charMbr)
        {
            this.charMbr = charMbr;
        }

        public int getIntMbr()
        {
            return intMbr;
        }

        public void setIntMbr(int intMbr)
        {
            this.intMbr = intMbr;
        }

        public Date getDate()
        {
            return date;
        }

        public void setDate(Date date)
        {
            this.date = date;
        }

        public ArrayList<Date> getArrayList()
        {
            return arrayList;
        }

        public void setArrayList(ArrayList<Date> lArrayList)
        {
            this.arrayList = lArrayList;
        }

        public LinkedList<String> getLinkedList()
        {
            return linkedList;
        }

        public void setLinkedList(LinkedList<String> lLinkedList)
        {
            this.linkedList = lLinkedList;
        }

        public LinkedHashMap<String, Date> getLinkedMap()
        {
            return linkedMap;
        }

        public void setLinkedMap(LinkedHashMap<String, Date> linkedMap)
        {
            this.linkedMap = linkedMap;
        }

		public Boolean getFalse1()
        {
			return false1;
		}

		public void setFalse1(Boolean false1)
        {
			this.false1 = false1;
		}

		public Boolean getFalse2()
        {
			return false2;
		}

		public void setFalse2(Boolean false2)
        {
			this.false2 = false2;
		}

		public Boolean getFalse3()
        {
			return false3;
		}

		public void setFalse3(Boolean false3)
        {
			this.false3 = false3;
		}

		public Boolean getTrue1()
        {
			return true1;
		}

		public void setTrue1(Boolean true1)
        {
			this.true1 = true1;
		}

		public Boolean getTrue2()
        {
			return true2;
		}

		public void setTrue2(Boolean true2)
        {
			this.true2 = true2;
		}

		public Boolean getTrue3()
        {
			return true3;
		}

		public void setTrue3(Boolean true3)
        {
			this.true3 = true3;
		}

        public TheSimpsons getSimpson()
        {
            return simpson;
        }

        public void setSimpson(TheSimpsons simpson)
        {
            this.simpson = simpson;
        }
    }

    public static class MyDate
    {
        private Date theDate;
        private String theTimeZone;

        @JSONConstruct
        public MyDate(long aTime, String aTimeZone)
        {
            theDate = new Date(aTime);
            theTimeZone = aTimeZone;
        }

        @JSONMap
        public Object[] getTime()
        {
            return new Object[]{theDate.getTime(), theTimeZone};
        }
    }

    public static class MyPojo
    {
        private String firstName;
        private String lastName;

        public void setNames(String aFirst, String aLast)
        {
            firstName = aFirst;
            lastName = aLast;
        }

        public String toString()
        {
            return "MyPojo{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    public MapperTest(String lName)
    {
        super(lName);
    }

    public void testIt()
    {
        try
        {
            MapperTest.TestBean lDuupje = new MapperTest.TestBean();
            lDuupje.setIntegerMbr(new Integer(13));
            lDuupje.setIntMbr(17);
            lDuupje.setShortMbr(new Short((short) 17));
            lDuupje.setStringMbr("It is not fair!");
            lDuupje.setBooleanMbr(true);
            lDuupje.setByteMbr((byte) 32);
            lDuupje.setLongMbr(12345l);
            lDuupje.setFloatMbr(123.12f);
            lDuupje.setDoubleMbr(987.89);
            lDuupje.setBigIntMbr(new BigInteger("123654789555"));
            lDuupje.setBigDecimalMbr(new BigDecimal("1111111465465.676476545"));
            lDuupje.setCharMbr('A');
            lDuupje.setDate(new Date());
            Boolean trueBoolean=new Boolean(true);
            Boolean falseBoolean=new Boolean(false);
            lDuupje.setTrue1(trueBoolean);
            lDuupje.setTrue2(trueBoolean);
            lDuupje.setTrue3(trueBoolean);
            lDuupje.setFalse1(falseBoolean);
            lDuupje.setFalse2(falseBoolean);
            lDuupje.setFalse3(falseBoolean);
            lDuupje.setSimpson(TheSimpsons.HOMER);
            
            LinkedList lLinkedList = new LinkedList();
            lLinkedList.add("uno");
            lLinkedList.add("duo");
            lDuupje.setLinkedList(lLinkedList);

            ArrayList<Date> lArrayList = new ArrayList<Date>();
            lArrayList.add(new Date());
            lArrayList.add(new Date());
            lDuupje.setArrayList(lArrayList);

            LinkedHashMap<String, Date> lMap = new LinkedHashMap<String, Date>();
            lMap.put("uno", new Date());
            lMap.put("duo", new Date());
            lDuupje.setLinkedMap(lMap);

            JSONValue lObj = JSONMapper.toJSON(lDuupje);
            String toJS=lObj.render(true);
            System.out.println(toJS);
            
            String fromJS=toJS.replaceAll("onlyReadableProperty","onlyWritableProperty");
            fromJS=fromJS.replaceAll("read me","changed me");
            Reader stringReader=new StringReader(fromJS);			
			JSONParser jsonParser=new JSONParser(stringReader,fromJS); 
			lObj=jsonParser.nextValue();
			
            MapperTest.TestBean lLitmus = (MapperTest.TestBean) JSONMapper.toJava(lObj, TestBean.class);
            Assert.assertNotNull(lLitmus);
//            assertEquals("changed me",lLitmus.onlyWritableProperty);
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            Assert.fail();
        }
    }
    
    public static class Graph
    {
    	private HashMap<String, ArrayList<Integer>> nodes;
    	private ArrayList edges;
    	private Collection<HashMap<String,String>> col;
    	
		public ArrayList getEdges()
        {
			return edges;
		}

        public void setEdges(ArrayList edges)
        {
			this.edges = edges;
		}

        public HashMap<String, ArrayList<Integer>> getNodes()
        {
			return nodes;
		}

        public void setNodes(HashMap<String, ArrayList<Integer>> nodes)
        {
			this.nodes = nodes;
		}

        public Collection<HashMap<String, String>> getCol()
        {
			return col;
		}

        public void setCol(Collection<HashMap<String, String>> col)
        {
			this.col = col;
		}
    }

    public void test2()
    {
    	HashMap<String, ArrayList<Integer>> nodes = new HashMap<String, ArrayList<Integer>>();

    	ArrayList<Integer> nodeInfo = new ArrayList<Integer>();
    	nodeInfo.add(new Integer(270));
    	nodeInfo.add(new Integer(360));

    	nodes.put("uniqueNodeId1", nodeInfo);
    	Collection<HashMap<String,String>> collection=new Vector<HashMap<String,String>>();
    	HashMap<String,String> hashMap=new HashMap<String, String>();
    	hashMap.put("index1","value1");
    	collection.add(hashMap);
    	
    	Graph graph = new Graph();
    	graph.setNodes(nodes);
    	graph.setEdges(new ArrayList());
    	graph.setCol(collection);
    	JSONValue lObj = null;
    	try {
    	lObj = JSONMapper.toJSON(graph);
    	}
    	catch (MapperException e) {
    	e.printStackTrace();
    	}

    	System.out.println(lObj.render(true)); // works

    	Object javaObj = null;
    	try {
    	javaObj = JSONMapper.toJava(lObj, graph.getClass());
    	Graph graph2=(Graph)javaObj;
    	HashMap<String, ArrayList<Integer>> nodes2=graph2.getNodes();
    	ArrayList<Integer> nodeInfo2=nodes2.get("uniqueNodeId1");
    	Iterator<Integer> iterator=nodeInfo2.iterator();
    	while(iterator.hasNext()){
    		System.out.println(iterator.next());	
    	}
    	
    	}
    	catch (Exception e) {
        e.printStackTrace();
            Assert.fail();
        }

    	System.out.println(javaObj);
    }

    public void test3(){
    	String[] strings={"abc","bcd","def"};
    	System.out.println("String[] class:"+strings.getClass());
    	JSONValue lObj = null;
    	try {
        	lObj = JSONMapper.toJSON(strings);
        }catch (MapperException e) {
        	e.printStackTrace();
            Assert.fail();
        }
        System.out.println(lObj.render(true)); 
        
        Object javaObj = null;
        try {
        	javaObj = JSONMapper.toJava(lObj, strings.getClass());
        	String[] strings2=(String[])javaObj;
        	System.out.println(strings2[0]+strings2[1]+strings2[2]);
        }
       	catch (Exception e) {
        	e.printStackTrace();
               Assert.fail();
        }
    }
    public static class TestBean4
    {
        private String myString;
        
        public TestBean4(){
        	this.myString="";
        }
		public TestBean4(String myString) {
			super();
			this.myString = myString;
		}

		public String getMyString() {
			return myString;
		}

		public void setMyString(String myString) {
			this.myString = myString;
		}
    }

    public void test4()
    {
        try
        {
            TestBean4[] bean4s = {new TestBean4("abc"), new TestBean4("bcd"), new TestBean4("def")};
            JSONValue lObj = null;
            lObj = JSONMapper.toJSON(bean4s);
            System.out.println(lObj.render(true));
            Object javaObj = null;
            javaObj = JSONMapper.toJava(lObj, bean4s.getClass());
            TestBean4[] beans = (TestBean4[]) javaObj;
            System.out.println(beans[0].getMyString() + beans[1].getMyString() + beans[2].getMyString());
        }
        catch(MapperException e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void test5()
    {
        try
        {
            String[][] strings = {{"abc", "bcd", "def"}, {"abc", "bcd", "def"}};
            System.out.println("String[] class:" + strings.getClass());
            JSONValue lObj = null;
            lObj = JSONMapper.toJSON(strings);
            System.out.println(lObj.render(true));
            Object javaObj = null;
            javaObj = JSONMapper.toJava(lObj, strings.getClass());
            String[][] strings2 = (String[][]) javaObj;
            System.out.println(strings2[0][0]+strings2[0][1]+strings2[0][2]);
        }
        catch(MapperException e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public static class SetAndListBean
    {
    	private Set<String> stringSet;
    	private List<String> stringList;
		public List<String> getStringList()
        {
			return stringList;
		}
		public void setStringList(List<String> stringList)
        {
			this.stringList = stringList;
		}
		public Set<String> getStringSet()
        {
			return stringSet;
		}
		public void setStringSet(Set<String> stringSet)
        {
			this.stringSet = stringSet;
		}
    }

    public void testSetAndList()
    {
        try
        {
            SetAndListBean setAndListBean = new SetAndListBean();
            Set<String> stringSet = new HashSet<String>();
            stringSet.add("abc");
            stringSet.add("bcd");
            List<String> stringList = new Vector<String>();
            stringList.add("abc");
            stringList.add("bcd");
            setAndListBean.setStringSet(stringSet);
            setAndListBean.setStringList(stringList);

            JSONValue jsonValue = JSONMapper.toJSON(setAndListBean);
            System.out.println(jsonValue.render(true));
            Object object = JSONMapper.toJava(jsonValue, setAndListBean.getClass());
            SetAndListBean setAndListBean2 = (SetAndListBean) object;
            Iterator<String> iterator = setAndListBean2.getStringList().iterator();
            System.out.println(iterator.next());
            System.out.println(iterator.next());
            iterator = setAndListBean2.getStringSet().iterator();
            System.out.println(iterator.next());
            System.out.println(iterator.next());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testDateMapper()
    {
        try
        {
            //By Default,DateMapper will ignore the timezone.
            //it's convenient for me,and maybe others.
            Date date1 = new Date();
            JSONValue lObj = JSONMapper.toJSON(date1);
            System.out.println(lObj.render(true));
            Object javaObj = JSONMapper.toJava(lObj, date1.getClass());
            Date date2 = (Date) javaObj;
            Assert.assertEquals(date1, date2);
            System.out.println(date2);

            DateMapper.setTimeZoneIgnored(false);
            date1 = new Date();
            lObj = JSONMapper.toJSON(date1);
            System.out.println(lObj.render(true));
            javaObj = JSONMapper.toJava(lObj, date1.getClass());
            date2 = (Date) javaObj;
            Assert.assertEquals(date1, date2);
            System.out.println(date2);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

    public void testAnnotatedMapper()
    {
        try
        {
            // Map fields, not properties.
            JSONMapper.usePojoAccess();
            MyDate lMyDate = new MyDate(new Date().getTime(), "CEST");
            JSONValue lObj = JSONMapper.toJSON(lMyDate);
            System.out.println(lObj.render(true));
            Object javaObj = JSONMapper.toJava(lObj, MyDate.class);
        }
        catch(MapperException e)
        {
            e.printStackTrace();
            Assert.fail();
        }      
    }

    public void testDirectMapper()
    {
       try
        {
            // Map fields, not properties.
            JSONMapper.usePojoAccess();
            MyPojo lPojo = new MyPojo();
            lPojo.setNames("Homer", "Simpson");          
            JSONValue lObj = JSONMapper.toJSON(lPojo);
            System.out.println(lObj.render(true));
            Object javaObj = JSONMapper.toJava(lObj, MyPojo.class);
            System.out.println(javaObj.toString());
        }
        catch(MapperException e)
        {
            e.printStackTrace();
            Assert.fail();
        }
    }

     public void testRepository()
     {
         System.out.println(JSONMapper.getRepository().prettyPrint());
     }
}
