cmake_minimum_required(VERSION 3.22.1)
project("cashflow-android-native" VERSION "1.0.0")

# Add library
file(GLOB SOURCE_FILES "${CMAKE_CURRENT_SOURCE_DIR}/*.c*")
add_library(${CMAKE_PROJECT_NAME} SHARED ${SOURCE_FILES})
target_link_libraries(${CMAKE_PROJECT_NAME} android log)