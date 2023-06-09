# Build
custom_build(
    # Name of the container image
    ref = 'catalog-service',
    # Command to build the container image
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    # Files to watch that trigger a new build
    deps = ['build.gradle', 'src']
)
# Deploy
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])
k8s_yaml(['../deployment/kubernetes/platform/development/services/postgresql.yml'])
# k8s_yaml(['../config-service/k8s/deployment.yml', '../config-service/k8s/service.yml'])
# Manage
k8s_resource('catalog-service', port_forwards=['9001'])
k8s_resource('polar-postgres', port_forwards=['5432'])
