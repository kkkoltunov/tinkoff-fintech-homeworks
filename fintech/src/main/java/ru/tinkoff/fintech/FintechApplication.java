package ru.tinkoff.fintech;

import java.util.*;
import java.util.stream.Collectors;

public class FintechApplication {

	public static void main(String[] args) {
		System.out.println("Students list:");
		for (Student student : students) {
			System.out.println(student);
		}
		System.out.println();
		
		sumOfAgesForName1();
		sumOfAgesForName2();
		sumOfAgesForNotExistsName();
		getSetOfNameStudents();
		findAnyStudentWithSelectedAge();
		findAnyNotExistsStudentWithSelectedAge();
		createMapWithIdAndName();
		createMapWithAgeAndCollectionOfStudentsSuchAge();
	}

	private static final List<Student> students = new ArrayList<>(List.of(
			new Student("Kirill", 22),
			new Student("Vasya", 17),
			new Student("Maria", 23),
			new Student("Kirill", 17)
	));

	private static void sumOfAgesForName1() {
		String nameForFind = "Kirill";
		int expectedAge = 39;

		int sumOfAges = students.stream()
				.filter(s -> Objects.equals(s.getName(), nameForFind))
				.mapToInt(Student::getAge).sum();

		if (expectedAge == sumOfAges) {
			System.out.println("The 1st age sum test passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in 1st age sum test!");
		}
	}

	private static void sumOfAgesForName2() {
		String nameForFind = "Maria";
		int expectedAge = 23;

		int sumOfAges = students.stream()
				.filter(s -> Objects.equals(s.getName(), nameForFind))
				.mapToInt(Student::getAge).sum();

		if (expectedAge == sumOfAges) {
			System.out.println("The 2nd age sum test passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in 2nd age sum test!");
		}
	}

	private static void sumOfAgesForNotExistsName() {
		String nameForFind = "Unknown";
		int expectedAge = 0;

		int sumOfAges = students.stream()
				.filter(s -> Objects.equals(s.getName(), nameForFind))
				.mapToInt(Student::getAge).sum();

		if (expectedAge == sumOfAges) {
			System.out.println("The 3rd age sum test passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in 3rd age sum test!");
		}
	}

	private static void getSetOfNameStudents() {
		Set<String> expectedNames = new HashSet<>(List.of(
				"Kirill",
				"Vasya",
				"Maria"
		));

		Set<String> nameOfStudents = students.stream()
				.map(Student::getName)
				.collect(Collectors.toSet());
		if (expectedNames.equals(nameOfStudents)) {
			System.out.println("The test for get a set of students names passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in test for get a set of students names!");
		}
	}

	private static void findAnyStudentWithSelectedAge() {
		int setAge = 20;
		boolean expectedResult = true;

		boolean studentWithSelectAge = students.stream()
				.anyMatch(s -> s.getAge() > setAge);

		if (expectedResult == studentWithSelectAge) {
			System.out.println("The 1st test for find any student with select age passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in 1st test for find any student with select age!");
		}
	}

	private static void findAnyNotExistsStudentWithSelectedAge() {
		int setAge = 50;
		boolean expectedResult = false;

		boolean studentWithSelectAge = students.stream()
				.anyMatch(s -> s.getAge() > setAge);

		if (expectedResult == studentWithSelectAge) {
			System.out.println("The 2nd test for find any student with select age passed successfully!");
		} else {
			throw new RuntimeException("Something wrong in 2nd test for find any student with select age!");
		}
	}

	private static void createMapWithIdAndName() {
		Map<String, String> expectedMap = new LinkedHashMap<>();
		expectedMap.put(students.get(0).getId(), "Kirill");
		expectedMap.put(students.get(1).getId(), "Vasya");
		expectedMap.put(students.get(2).getId(), "Maria");
		expectedMap.put(students.get(3).getId(), "Kirill");

		Map<String, String> mapStudents = students.stream()
				.collect(Collectors.toMap(Student::getId, Student::getName));

		if (expectedMap.equals(mapStudents)) {
			System.out.println("The test for create map with id and name passed successfully!");
			System.out.println(mapStudents + "\n");
		} else {
			throw new RuntimeException("Something wrong in test for create map with id and name!");
		}
	}

	private static void createMapWithAgeAndCollectionOfStudentsSuchAge() {
		Map<Integer, Set<Student>> expectedMap = new LinkedHashMap<>();
		expectedMap.put(17, new HashSet<>(List.of(
				new Student("Vasya", 17),
				new Student("Kirill", 17)
		)));
		expectedMap.put(22, new HashSet<>(List.of(
				new Student("Kirill", 22)
		)));
		expectedMap.put(23, new HashSet<>(List.of(
				new Student("Maria", 23)
		)));

		Map<Integer, Set<Student>> mapStudents = students.stream()
				.collect(Collectors.groupingBy(Student::getAge, Collectors.toSet()));

		if (expectedMap.equals(mapStudents)) {
			System.out.println("The test for create map with age and collection of students such age passed successfully!");
			System.out.println(mapStudents + "\n");
		} else {
			throw new RuntimeException("Something wrong in test for create map with age and collection of students such age!");
		}
	}
}
