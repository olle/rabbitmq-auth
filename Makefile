.PHONY: run verbose debug

run:
	@mvn clean spring-boot:run

verbose:
	@mvn clean spring-boot:run -Ddebug=true

debug:
	@mvnDebug clean spring-boot:run
