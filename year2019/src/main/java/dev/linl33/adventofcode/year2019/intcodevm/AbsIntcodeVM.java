//package dev.linl33.adventofcode.year2019.intcodevm;
//
//import dev.linl33.adventofcode.year2019.intcodevm.register.IRegister;
//import dev.linl33.adventofcode.year2019.intcodevm.register.RegisterMap;
//
//import java.util.Optional;
//import java.util.concurrent.BlockingDeque;
//
//public abstract class AbsIntcodeVM<TData, TAddr> implements IIntcodeVm<TData> {
//  public abstract IntcodeMemory<TData, TAddr> getMemory();
//
//  public abstract <T> RegisterMap<? extends Class<? extends IRegister<T>>, ? extends MemoryAccessor<T>, T> getRegisters();
////  public abstract <T> RegisterMemory<? extends Class<? extends IRegister<T>>, ? extends MemoryAccessor<T>, T> getRegisters();
////  public abstract <T> EnumRegMap<? extends MemoryAccessor<T>, T> getRegisters();
//
//  public abstract Optional<TData> getVerb();
//
//  public abstract void setVerb(TData verb);
//
//  public abstract Optional<TData> getNoun();
//
//  public abstract void setNoun(TData noun);
//
//  public abstract BlockingDeque<TData> getInput();
//
//  public abstract BlockingDeque<TData> getOutput();
//
//  public abstract State getState();
//
//  public abstract void setState(State state);
//
//  public abstract void suspendVM();
//
//  public abstract void resumeVM();
//
//  public abstract IntcodeMemory<TData, TAddr> copyMemory();
//
//  public abstract <T> RegisterMap<? extends Class<? extends IRegister<T>>, ? extends MemoryAccessor<T>, T> copyRegisters();
////  public abstract <T> EnumRegMap<? extends MemoryAccessor<T>, T> copyRegisters();
//
//  public abstract void resetPc();
//
//  public abstract State findState();
//
//  public abstract void interpreterLoop(Runnable runnable);
//
//  IntcodeMemory<TData, TAddr> executeInternal(ExecMode mode, boolean nonBlocking) {
//    if (getState() == State.HALT) {
//      setState(State.NULL);
//    }
//
//    if (getState() == State.SUSPEND) {
//      resumeVM();
//    }
//
//    if (!nonBlocking) {
//      setState(State.BLOCKING);
//    }
//
//    var memory = mode == ExecMode.STATELESS ? copyMemory() : getMemory();
//    RegisterMap<? extends Class<? extends IRegister<Object>>, ? extends MemoryAccessor<Object>, Object> objectRegisterMap = mode == ExecMode.STATELESS ? copyRegisters() : getRegisters();
//
//
//    if (getState() == State.BLOCKING || getState() == State.NULL) {
//      resetPc();
//    }
//
//    getVerb().ifPresent(memory::writeVerb);
//    getNoun().ifPresent(memory::writeNoun);
//
//    interpreterLoop(() -> {
//      new MemoryAccessor<TData>() {
//        @Override
//        public TData get() {
//          return memory.read();
//        }
//
//        @Override
//        public TData set(TData newVal) {
//          return null;
//        }
//
//        @Override
//        public TData add(TData delta) {
//          return null;
//        }
//      }
//    });
//
//    setState(findState());
//
//    return memory;
//  }
//}
