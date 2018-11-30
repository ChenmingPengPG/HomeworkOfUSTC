ADD_EXECUTABLE(useHello useHello.cpp)
INCLUDE_DIRECTORIES(/usr/include/hello)
TARGET_LINK_LIBRARIES(useHello libhello.so)
