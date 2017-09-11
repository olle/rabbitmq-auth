.PHONY: run debug

run:
	@mvn clean spring-boot:run

debug:
	@mvnDebug cleanclean spring-boot:run
