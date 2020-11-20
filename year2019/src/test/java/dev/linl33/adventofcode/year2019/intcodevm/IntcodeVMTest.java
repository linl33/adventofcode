//package dev.linl33.adventofcode.year2019.intcodevm;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.Arrays;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//
//class IntcodeVMTest {
//  @ParameterizedTest(name = "{0} => {1}")
//  @MethodSource
//  void executeStateless(int[] memory, int result) {
//    var verb = memory[1];
//    var noun = memory[2];
//
//    var vm = new IntcodeVM(memory);
//    var memStr = vm.printMemory();
//    var regStr = vm.printRegisters();
//
//    assertEquals(result, vm.execute(verb, noun, IntcodeVM.ExecMode.STATELESS));
//    assertEquals(memStr, vm.printMemory());
//    assertEquals(regStr, vm.printRegisters());
//
//    assertEquals(result, vm.executeNonBlocking(verb, noun, IntcodeVM.ExecMode.STATELESS));
//    assertEquals(memStr, vm.printMemory());
//    assertEquals(regStr, vm.printRegisters());
//  }
//
//  static Stream<Arguments> executeStateless() {
//    return Stream.of(
//        arguments(new int[]{1, 0, 0, 0, 99}, 2),
//        arguments(new int[]{2, 3, 0, 3, 99}, 2),
//        arguments(new int[]{2, 4, 4, 5, 99, 0}, 2),
//        arguments(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99}, 30)
//    );
//  }
//
//  @ParameterizedTest(name = "{0} => {1}")
//  @MethodSource
//  void executeStateful(int[] memory, int[] memoryAfterExec) {
//    assertEquals(
//        Arrays.toString(memoryAfterExec),
//        new IntcodeVM(memory)
//            .execute(IntcodeVM.ExecMode.STATEFUL)
//            .printMemory()
//    );
//  }
//
//  static Stream<Arguments> executeStateful() {
//    return Stream.of(
//        arguments(new int[]{1, 0, 0, 0, 99}, new int[]{2, 0, 0, 0, 99}),
//        arguments(new int[]{2, 3, 0, 3, 99}, new int[]{2, 3, 0, 6, 99}),
//        arguments(new int[]{2, 4, 4, 5, 99, 0}, new int[]{2, 4, 4, 5, 99, 9801}),
//        arguments(new int[]{1, 1, 1, 4, 99, 5, 6, 0, 99}, new int[]{30, 1, 1, 4, 2, 5, 6, 0, 99}),
//        arguments(new int[]{1002, 4, 3, 4, 33}, new int[]{1002, 4, 3, 4, 99})
//    );
//  }
//
//  @TestFactory
//  Stream<DynamicNode> executeInputOutput_equal8() {
//    return Stream
//        .of(
//            new int[]{3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8}, // Position Mode
//            new int[]{3, 3, 1108, -1, 8, 3, 4, 3, 99} // Immediate Mode
//        )
//        .map(vm -> DynamicContainer.dynamicContainer(
//            "VM " + Arrays.toString(vm),
//            Stream
//                .of(
//                    "8, 1",
//                    "-8, 0",
//                    "10, 0",
//                    "0, 0",
//                    "-1, 0"
//                )
//                .map(str -> str.split(", "))
//                .map(split -> new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])})
//                .map(pair -> DynamicTest.dynamicTest(
//                    pair[0] + " => " + pair[1],
//                    () -> assertEquals(
//                        pair[1],
//                        new IntcodeVM(vm)
//                            .execute(pair[0], IntcodeVM.ExecMode.STATELESS)
//                            .getOutput()
//                            .getLast()
//                    )
//                ))
//        ));
//  }
//
//  @TestFactory
//  Stream<DynamicNode> executeInputOutput_lessThan8() {
//    return Stream
//        .of(
//            new int[]{3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8}, // Position Mode
//            new int[]{3, 3, 1107, -1, 8, 3, 4, 3, 99} // Immediate Mode
//        )
//        .map(vm -> DynamicContainer.dynamicContainer(
//            "VM " + Arrays.toString(vm),
//            Stream
//                .of(
//                    "8, 0",
//                    "-8, 1",
//                    "10, 0",
//                    "0, 1",
//                    "7, 1",
//                    "9, 0"
//                )
//                .map(str -> str.split(", "))
//                .map(split -> new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])})
//                .map(pair -> DynamicTest.dynamicTest(
//                    pair[0] + " => " + pair[1],
//                    () -> assertEquals(
//                        pair[1],
//                        new IntcodeVM(vm)
//                            .execute(pair[0], IntcodeVM.ExecMode.STATELESS)
//                            .getOutput()
//                            .getLast()
//                    )
//                ))
//        ));
//  }
//
//  @TestFactory
//  Stream<DynamicNode> executeInputOutput_jmp() {
//    return Stream
//        .of(
//            new int[]{3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9}, // Position Mode
//            new int[]{3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1} // Immediate Mode
//        )
//        .map(vm -> DynamicContainer.dynamicContainer(
//            "VM " + Arrays.toString(vm),
//            Stream
//                .of(
//                    "0, 0",
//                    "1, 1",
//                    "-1, 1",
//                    "100, 1",
//                    "-100, 1"
//                )
//                .map(str -> str.split(", "))
//                .map(split -> new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])})
//                .map(pair -> DynamicTest.dynamicTest(
//                    pair[0] + " => " + pair[1],
//                    () -> assertEquals(
//                        pair[1],
//                        new IntcodeVM(vm)
//                            .execute(pair[0], IntcodeVM.ExecMode.STATELESS)
//                            .getOutput()
//                            .getLast()
//                    )
//                ))
//        ));
//  }
//
//  @ParameterizedTest
//  @CsvSource({
//      "7, 999",
//      "8, 1000",
//      "9, 1001",
//      "-100, 999",
//      "100, 1001"
//  })
//  void executeInputOutput_jmpLarge(int input, int expected) {
//    assertEquals(
//        expected,
//        new IntcodeVM(new int[] {
//            3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
//            1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
//            999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99
//        })
//        .execute(input, IntcodeVM.ExecMode.STATELESS)
//        .getOutput()
//        .getLast()
//    );
//  }
//}
