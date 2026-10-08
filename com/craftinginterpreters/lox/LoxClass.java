package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable {
  final String name;
  private final Map<String, LoxFunction> methods;
  private final Map<String, LoxFunction> staticMethods;

  LoxClass(String name, Map<String, LoxFunction> methods,
           Map<String, LoxFunction> staticMethods) {
    super(null);
    this.name = name;
    this.methods = methods;
    this.staticMethods = staticMethods;
  }

  @Override
  Object get(Token token, Interpreter interpreter) {
    LoxFunction method = staticMethods.get(token.lexeme);
    if (method != null) {
      if (method.isGetter()) return method.call(interpreter, java.util.Collections.emptyList());
      return method;
    }
    throw new RuntimeError(token, "Undefined property '" + token.lexeme + "'.");
  }

  @Override
  public int arity() {
    LoxFunction initializer = findMethod("init");
    return initializer == null ? 0 : initializer.arity();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    LoxInstance instance = new LoxInstance(this);
    LoxFunction initializer = findMethod("init");
    if (initializer != null) initializer.bind(instance).call(interpreter, arguments);
    return instance;
  }

  LoxFunction findMethod(String name) {
    return methods.get(name);
  }

  @Override
  public String toString() {
    return name;
  }
}
