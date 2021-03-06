package fr.nico.sqript.expressions;

import fr.nico.sqript.compiling.ScriptException;
import fr.nico.sqript.meta.Expression;
import fr.nico.sqript.structures.ScriptContext;
import fr.nico.sqript.types.interfaces.IIndexedCollection;
import fr.nico.sqript.types.ScriptType;
import fr.nico.sqript.types.TypeArray;
import fr.nico.sqript.types.TypeDictionary;
import fr.nico.sqript.types.primitive.TypeBoolean;
import fr.nico.sqript.types.primitive.TypeNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

@Expression(name = "Arrays",
        description = "Utilities about arrays",
        examples = "size of array",
        patterns = {
                "~[{element*}~]:array",
                "size of {array}:number",
                "[a] random element of {array}:element",
                "first element of {array}:element",
                "last element of {array}:element",
                "{+array}~[{+number}~]:element",
                "numbers from {number} to {number}:array",
                "[numbers] in range of {number}:array",
                "random numbers in range of {number}:array",
                "{element} is in {array}:boolean",
                "{element} is not in {array}:boolean",
                "{array} contains {element}:boolean",

        },
        priority = -1
)
public class ExprArrays extends ScriptExpression{



    @Override
    public ScriptType get(ScriptContext context, ScriptType[] parameters) {
        switch(getMatchedIndex()){
            case 0://new array
                TypeArray array = new TypeArray();
                array.getObject().addAll(Arrays.asList(parameters));
                return array;
            case 1: //size of array
                IIndexedCollection a = (IIndexedCollection) parameters[0];
                return new TypeNumber(a.size());
            case 2: //random element of
                a = (IIndexedCollection) parameters[0];
                return a.get(new Random().nextInt(a.size()));
            case 3: //first element of
                a = (IIndexedCollection) parameters[0];
                return a.get(0);
            case 4: //last element of
                a = (IIndexedCollection) parameters[0];
                return a.get(a.size()-1);
            case 5: //array[int] expression
                if(parameters[0] instanceof TypeArray){
                    a = (IIndexedCollection) parameters[0];
                    TypeNumber n = (TypeNumber) parameters[1];
                    return a.get(n.getObject().intValue());
                }else if(parameters[0] instanceof TypeDictionary){
                    TypeDictionary d = (TypeDictionary) parameters[0]; //A simple wrapper for the HashMap
                    return d.getObject().get(parameters[1]);
                }
                break;
            case 6: //numbers from a to b
                array = new TypeArray();
                TypeNumber b1 = (TypeNumber) parameters[0];
                TypeNumber b2 = (TypeNumber) parameters[1];
                for(double i = b1.getObject();i<=b2.getObject();i++){
                    array.getObject().add(new TypeNumber(i));
                }
                return array;
            case 7: //numbers in range of a
                array = new TypeArray();
                b1 = (TypeNumber) parameters[0];
                for(double i = 0;i<=b1.getObject();i++){
                    array.getObject().add(new TypeNumber(i));
                }
                return array;
            case 8: //random numbers in range of a
                array = new TypeArray();
                b1 = (TypeNumber) parameters[0];
                for(double i = 0;i<=b1.getObject();i++){
                    array.getObject().add(new TypeNumber(i));
                }
                Collections.shuffle(array.getObject());
                return array;
            case 9: //element is in
                ScriptType b = parameters[0];
                array = (TypeArray) parameters[1];
                for(ScriptType i : array.getObject()){
                    if(b.equals(i))return TypeBoolean.TRUE();
                }
                return TypeBoolean.FALSE();
            case 10: //element is not in
                b = parameters[0];
                array = (TypeArray) parameters[1];
                for(ScriptType i : array.getObject()){
                    if(b.equals(i))return TypeBoolean.FALSE();
                }
                return TypeBoolean.TRUE();
            case 11://contains
                b = parameters[1];
                array = (TypeArray) parameters[0];
                for(ScriptType i : array.getObject()){
                    if(b.equals(i))return TypeBoolean.TRUE();
                }
                return TypeBoolean.FALSE();

        }
        return null;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public boolean set(ScriptContext context, ScriptType to, ScriptType[] parameters) throws ScriptException.ScriptUndefinedReferenceException {
        switch(getMatchedIndex()){
            case 5:
                TypeArray a = (TypeArray) parameters[0];
                TypeNumber b = (TypeNumber) parameters[1];
                a.getObject().set(b.getObject().intValue(),to);
                return true;
        }
        return false;
    }
}
